package com.ourdream.service;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.ourdream.beans.Cet;
import com.ourdream.dao.CetDAO;

/**
 * 
 * @author kylin 这是cet的服务层，向servlet提供服务
 */
public class CetService {

	private Cet cet;
	private CetDAO cetDao;

	public CetService(Cet cet) {
		this.cet = cet;
		this.cetDao = new CetDAO();
	}

	/**
	 * 这个方法的作用是将Dao层提供的Cet对象转化为json字符串返回
	 * 
	 * @return String json data
	 * @throws IOException
	 */
	public String sendJson() throws IOException {
		cet = cetDao.getCet(cet);
		// 最外面的对象 { }
		JsonObject object = new JsonObject();
		// 里面是cet object
		JsonObject cetObject = new JsonObject();
		// 给cetObject添加属性
		cetObject.addProperty("id", cet.getId());
		cetObject.addProperty("name", cet.getName());
		if (cet.getError() == null) {
			cetObject.addProperty("school", cet.getSchool());
			cetObject.addProperty("category", cet.getCategory());
			cetObject.addProperty("date", cet.getDate());
			cetObject.addProperty("tatal", cet.getTatal());
			cetObject.addProperty("listening", cet.getListening());
			cetObject.addProperty("reading", cet.getReading());
			cetObject.addProperty("writingAndTranslation", cet.getWritingAndTranslation());
		} else {
			cetObject.addProperty("error", cet.getError());
		}
		// 将cetObject添加到object
		object.add("cet", cetObject);
		return object.toString();
	}

}
