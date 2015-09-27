package com.ourdream.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ourdream.beans.LastAnnouncement;
import com.ourdream.utils.JdbcUtil;
import com.ourdream.utils.JsoupUtil;

/**
 * 
 * @author ourdream 对获取的最新公告对象进行分装 存储到db中 提供获取最新公告对象的方法
 */
public class LastAnnouncementDAO {

	private String LastAnnUrl = "http://www.nwpu.edu.cn/xygg/homexx.htm";
	private QueryRunner qr;
	private Date lastDateInDB;
	private Date lastDateInHtml;

	public LastAnnouncementDAO() throws SQLException {
		lastDateInDB = getLastDateInDB();
		lastDateInHtml = getLastDateInHtml();
	}

	// 获取最新公告
	public List<LastAnnouncement> getLastAnnouncements() {
		String sql = "select * from LastAnnouncement";
		qr = JdbcUtil.getQueryRunner();
		try {
			List<LastAnnouncement> las = qr.query(sql, new BeanListHandler<LastAnnouncement>(LastAnnouncement.class));
			return las;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 向数据库中存储 从网站上获取的 最新公告
	 */
	public void save() {
		// 比较数据库和网页数据的最新时间 从而决定是否更新数据库
		if (lastDateInHtml.after(lastDateInDB)) {
			qr = JdbcUtil.getQueryRunner();
			String delSql = "delete from LastAnnouncement";
			String sql = "insert into LastAnnouncement(title,category,date,content) values(?,?,?,?)";
			// 在http://www.nwpu.edu.cn/xygg/homexx.htm上获取数据
			Document document;
			try {
				qr.update(delSql);
				document = Jsoup.connect(LastAnnUrl).header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE).get();
				Elements tables = document.getElementsByTag("table");
				// 获取罪行公告所在的table
				Element table = tables.get(1);
				// 获取所有的tr
				Elements trs = table.getElementsByTag("tr");
				// 遍历获取所有的tr节点
				for (Element tr : trs) {
					Elements as = tr.getElementsByTag("a");
					// 获取类别
					String category = as.first().text();
					// 去除[ ]
					category = category.substring(1, category.length() - 1);
					Element article = as.get(1);
					// 获取文章的title
					String title = article.text();
					// 获取文章的链接地址
					String url = article.absUrl("href");
					// 再次请求 获取文章的详细信息
					Document doc = Jsoup.connect(url).header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE).get();
					// 根据classname 获取元素节点
					Element timeElement = doc.getElementsByClass("timestyle58490").first();
					String dateStr = timeElement.text();
					Element contentElement = doc.getElementsByClass("contentstyle58490").first();
					// 获取文章内容 纯文本
					String content = contentElement.text();
					qr.update(sql, title, category, dateStr, content);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取网页内容的最新时间
	 * 
	 * @return lastDateInHtml
	 */
	private Date getLastDateInHtml() {
		Document document;
		try {
			document = Jsoup.connect(LastAnnUrl).header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE).get();
			Elements tables = document.getElementsByTag("table");
			// 获取罪行公告所在的table
			Element table = tables.get(1);
			// 获取所有的tr
			Elements trs = table.getElementsByTag("tr");
			Element tr = trs.first();
			Elements as = tr.getElementsByTag("a");
			Element article = as.get(1);
			String url = article.absUrl("href");
			Document doc = Jsoup.connect(url).header(JsoupUtil.AGENT, JsoupUtil.AGENT_VALUE).get();
			Element timeElement = doc.getElementsByClass("timestyle58490").first();
			String dateStr = timeElement.text();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Date date = sdf.parse(dateStr);
			return date;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取存入数据库中最新的记录的时间
	 * 
	 * @return lastDateInDB
	 * @throws SQLException
	 */
	private Date getLastDateInDB() throws SQLException {
		String sql = "select * from LastAnnouncement";
		qr = JdbcUtil.getQueryRunner();
		LastAnnouncement la = qr.query(sql, new BeanHandler<LastAnnouncement>(LastAnnouncement.class));
		return la.getDate();
	}
}
