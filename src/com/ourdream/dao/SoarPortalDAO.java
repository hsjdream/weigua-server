package com.ourdream.dao;

import java.io.File;
import java.io.IOException;

import org.jsoup.Connection;
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

		Connection conn = Jsoup.connect(url);
		conn.userAgent(JsoupUtil.AGENT_VALUE);
		conn.data("usename", username);
		conn.data("password", password);
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
		System.out.println(new File("").getAbsolutePath());
	}

}
