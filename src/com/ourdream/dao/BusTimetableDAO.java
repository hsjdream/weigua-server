package com.ourdream.dao;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ourdream.beans.BusTimetable;
import com.ourdream.utils.JsoupUtil;

public class BusTimetableDAO {
	// 获取数据的url 因为一共是一个数据 从而没有将内容存储到数据库中
	private String url = "http://www.nwpu.edu.cn/info/1003/15342.htm";

	/**
	 * 这个方法将url上的内容封装成为BusTimetable对象 注：这个对象中的content属性可以通过 '#' 进行切割
	 * 
	 * @return BusTimetable
	 * @throws IOException
	 */
	public BusTimetable getBusTimetable() throws IOException {
		// 创建对象
		BusTimetable bus = new BusTimetable();
		Connection conn = Jsoup.connect(url);
		conn.header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE);
		Document document = conn.get();
		// 获取title
		String title = document.getElementsByClass("titlestyle58490").first().text();
		bus.setTitle(title);
		// 获取时间
		String date = document.getElementsByClass("timestyle58490").first().text();
		bus.setDate(date);
		// 获取内容
		Element contentRoot = document.getElementById("vsb_content_2");
		StringBuffer content = new StringBuffer();
		for (Element e : contentRoot.children()) {
			content.append("#" + e.text());
		}
		bus.setContent(content.substring(1));
		return bus;
	}
}
