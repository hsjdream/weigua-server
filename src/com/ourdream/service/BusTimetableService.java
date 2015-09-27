package com.ourdream.service;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.ourdream.beans.BusTimetable;
import com.ourdream.dao.BusTimetableDAO;
/**
 * 
 * @author kylin
 * 为servlet提供服务，将BusTimetable对象转化为json数据发送
 */
public class BusTimetableService {

	private BusTimetableDAO busDao;

	public BusTimetableService() {
		busDao = new BusTimetableDAO();
	}

	/**
	 * 
	 * @return String json data
	 * @throws IOException
	 */
	public String sendJson() throws IOException {
		BusTimetable bus = busDao.getBusTimetable();
		// 创建Json root
		JsonObject object = new JsonObject();
		// 创建 bus对象
		JsonObject busObj = new JsonObject();
		busObj.addProperty("title", bus.getTitle());
		busObj.addProperty("date", bus.getDate());
		busObj.addProperty("conent", bus.getContent());
		// 将bus对象添加到 json root
		object.add("busTimetable", busObj);
		return object.toString();
	}

}
