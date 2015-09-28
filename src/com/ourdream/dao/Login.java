package com.ourdream.dao;

import java.awt.FlowLayout;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Login {

	private static CloseableHttpResponse response;
	private static HttpGet get;
	private static HttpPost post;
	private static String html;
	private static Document doc;
	private static Element a;
	private static String href;
	private static List<NameValuePair>  pairs;

	public static void main(String[] args) throws Exception {
		// 获取HttpClient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建get请求 验证码
		get = new HttpGet("http://cas.nwpu.edu.cn/cas/Captcha.jpg");
		// 将服务器返回的验证码保存到本地
		response = client.execute(get);
		FileOutputStream out = new FileOutputStream("1.jpg");
		response.getEntity().writeTo(out);
		// 将本地的验证码通过JFrame展现给用户 方便输入
		JFrame frame = new JFrame();
		frame.setVisible(false);
		frame.setLayout(new FlowLayout());
		JLabel label = new JLabel();
		Icon icon = new ImageIcon("1.jpg");
		label.setIcon(icon);
		frame.add(label);
		frame.setBounds(100, 100, 100, 100);
		frame.setVisible(true);
		// 用户在命令行输入验证码
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();

		/* 开始登陆翱翔门户 */
		// 访问翱翔门户的登录页面 get方式
		get = new HttpGet("http://cas.nwpu.edu.cn/cas/login?service=http%3A%2F%2Fportal.nwpu.edu.cn%2Fdcp%2Findex.jsp");
		CloseableHttpResponse r = client.execute(get);
		html = EntityUtils.toString(r.getEntity());
		// 通过Jsoup对获得的html进行解析
		Document document = Jsoup.parse(html);
		// 获取form节点
		Element form = document.getElementById("login_form");
		// 获取form中的所有input
		Elements inputs = form.getElementsByTag("input");
		// 创建post对象发送的键值对
		pairs = new ArrayList<NameValuePair>();
		// 对input进行遍历 主要是对隐藏的input进行遍历
		for (Element input : inputs) {
			if (input.attr("name").trim() != "") {
				String name = input.attr("name");
				if ("username".equals(name) || "password".equals(name) || "captcha".equals(name)) {
					continue;
				}
				// 将 name和value存放到 NameValuePair中
				String value = input.attr("value");
				pairs.add(new BasicNameValuePair(name, value));
			}
		}
		// 创建post请求，url是form表单最后提交的url
		post = new HttpPost("http://cas.nwpu.edu.cn/cas/login");
		// 将需要用户输入的数据加入到 key-value中
		pairs.add(new BasicNameValuePair("username", "2013303356"));
		pairs.add(new BasicNameValuePair("password", "254183"));
		pairs.add(new BasicNameValuePair("captcha", str));
		// 将上面的所有参数添加到post请求的 请求体中
		post.setEntity(new UrlEncodedFormEntity(pairs));
		// 获取 response
		response.close();
		// 提交表单数据 获取response
		response = client.execute(post);
		html = EntityUtils.toString(response.getEntity(), "UTF-8");
		doc = Jsoup.parse(html);
		a = doc.getElementsByTag("a").first();
		href = a.attr("href");

		// 请求提交表单返回的内容，主要是这里面有session，所以这个页面必须访问
		get = new HttpGet(href);
		response.close();
		response = client.execute(get);
		// 因为获取了 session 从而现在可以直接到目的页面 不会被重定向了
		// 进入到新教务系统 
		get = new HttpGet("http://portal.nwpu.edu.cn/dcp/forward.action?path=dcp/apps/sso/jsp/ssoDcpSelf&appid=37868");
		response.close();
		response = client.execute(get);
		// 从返回的html中可以看到 里面的链接包含了session 所以需要访问
		html = EntityUtils.toString(response.getEntity());
		doc = Jsoup.parse(html);
		a = doc.getElementsByTag("a").first();
		href = a.attr("href");
		get = new HttpGet(href);
		response.close();
		// 执行
		response = client.execute(get);
		
		// 执行刚刚进入课表的第一个url 必须执行这个url 否则不能够获取课表
		get = new HttpGet("http://us.nwpu.edu.cn/eams/courseTableForStd.action?_=" + new Date().getTime());
		response.close();
		response = client.execute(get);
		// 在html中寻找 ids属性值
		html = EntityUtils.toString(response.getEntity());
		//	bg.form.addInput(form,"ids","219410");
		String ids = "";
		String regex = "form,\"ids\",\"(\\d{6,}+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		if(matcher.find()){
			ids = matcher.group(1);
		}
		
		// 现在需要访问课表所在的url 
		post = new HttpPost("http://us.nwpu.edu.cn/eams/courseTableForStd!courseTable.action");
		pairs.clear();
		// post设置属性
		pairs.add(new BasicNameValuePair("ignoreHead", "1"));
		pairs.add(new BasicNameValuePair("setting.kind", "std"));
		pairs.add(new BasicNameValuePair("startWeek", "1"));
		pairs.add(new BasicNameValuePair("semester", "15"));
		pairs.add(new BasicNameValuePair("ids", ids));
		post.setEntity(new UrlEncodedFormEntity(pairs));
		response.close();
		response = client.execute(post);
		html = EntityUtils.toString(response.getEntity());
		doc = Jsoup.parse(html);
		Element table = doc.getElementById("manualArrangeCourseTable");
		Element root = table.getElementsByTag("tbody").first();
		Elements trs = root.children();
		for(Element tr : trs){
			
		}
	}

	public static void print(CloseableHttpResponse response) throws ParseException, IOException {
		System.out.println(response.getStatusLine().toString());
		Header[] headers = response.getAllHeaders();
		for (Header h : headers) {
			System.out.println(h.toString());
		} 
		System.out.println(EntityUtils.toString(response.getEntity()));
	}

}
