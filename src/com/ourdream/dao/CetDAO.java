package com.ourdream.dao;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ourdream.beans.Cet;
import com.ourdream.utils.JsoupUtil;

/**
 * @author kylin 这是cet的数据访问层对象，对数据进行基本的封装
 */
public class CetDAO {
	/**
	 * action=http://cet.99sushe.com/find method=post accept-charset=UTF-8
	 * 查询cet是通过从99宿舍网上获取的数据，然后对这个数据进行解析， 最后封装到Cet对象中返回
	 * 
	 * @return Cet
	 * @throws IOException
	 */
	public Cet getCet(Cet cet) throws IOException {
		// 拼接url
		String url = "http://www.chsi.com.cn/cet/query?zkzh=" + cet.getId() + "&xm=" + cet.getName();
		Connection conn = Jsoup.connect(url);
		// 设置这个请求的来源是哪里 很关键
		conn.header("Referer", "http://www.chsi.com.cn");
		// http请求头中的 user-agent 这个属性在这个网页中没有做限定
		conn.header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE);
		// 获取document对象
		Document document = conn.get();
		Element root = document.getElementsByClass("m_cnt_m").first();
		Element firstEle = root.child(0);
		String tagName = firstEle.tagName();
		if ("table".equals(tagName)) {
			Element info = document.getElementsByClass("cetTable").first();
			Element tbody = info.child(0);
			Elements trs = tbody.children();
			// 学校
			String school = trs.get(1).child(1).text();
			cet.setSchool(school);
			// 类别
			String category = trs.get(2).child(1).text();
			cet.setCategory(category);
			// 时间
			String date = trs.get(4).child(1).text();
			cet.setDate(date);
			String scoreStr = trs.get(5).child(1).text();
			String[] scoreStrs = scoreStr.split(" ");
			cet.setTatal(scoreStrs[0]);
			cet.setListening(scoreStrs[2]);
			cet.setReading(scoreStrs[4]);
			cet.setWritingAndTranslation(scoreStrs[6]);
		} else {
			String error = firstEle.text();
			cet.setError(error);
		}
		return cet;
	}
}
