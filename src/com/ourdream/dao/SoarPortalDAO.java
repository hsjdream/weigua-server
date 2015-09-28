package com.ourdream.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.ourdream.utils.JsoupUtil;

public class SoarPortalDAO {

	private String url = "http://cas.nwpu.edu.cn/cas/login";
	private String captchaUrl = "http://cas.nwpu.edu.cn/cas/Captcha.jpg";
	private String username = "2013303356";
	private String password = "254183";
	private String captcha = "";

	public String getURL() throws IOException {

		String s = "http://cas.nwpu.edu.cn/cas/login?service=http%3A%2F%2Fportal.nwpu.edu.cn%2Fdcp%2Findex.jsp";
		Connection cs = Jsoup.connect(s);
		Response response = cs.execute();

		Connection conn = Jsoup.connect(url);
		conn.userAgent(JsoupUtil.AGENT_VALUE);
		conn.data("usename", username);
		conn.data("password", password);
		conn.cookies(response.cookies());
		Connection c = Jsoup.connect(captchaUrl);
		Document document = conn.get();
		System.out.println(document);
		return null;
	}

	public String getCaptcha() throws IOException {
		Connection conn = Jsoup.connect(captchaUrl);
		conn.userAgent(JsoupUtil.AGENT_VALUE);
		Document document = conn.get();
		System.out.println(document);
		return null;
	}

	@Test
	public void test() throws IOException {
		URL url = new URL("http://cas.nwpu.edu.cn/cas/Captcha.jpg");
		InputStream in = url.openStream();
		byte[] buf = new byte[1024];
		int len = -1;
		OutputStream out = new FileOutputStream("1.jpg");
		while((len = in.read(buf)) != -1){
			out.write(buf, 0, len);
		}
		out.close();
		in.close();
	}
	
	@Test
	public void test1() throws IOException{
		URL url = new URL("http://cas.nwpu.edu.cn/cas/Captcha.jpg");
		URLConnection conn = url.openConnection();
		conn.getOutputStream();
	}

}
