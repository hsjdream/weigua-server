package com.ourdream.service;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ourdream.beans.LastAnnouncement;
import com.ourdream.dao.LastAnnouncementDAO;

public class LastAnnouncementService {

	public String sendJson() throws SQLException {
		LastAnnouncementDAO laDao = new LastAnnouncementDAO();
		laDao.save();
		List<LastAnnouncement> las = laDao.getLastAnnouncements();
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		for (LastAnnouncement la : las) {
			JsonObject o = new JsonObject();
			o.addProperty("title", la.getTitle());
			o.addProperty("category", la.getCategory());
			o.addProperty("date", la.getDate().toString());
			o.addProperty("content", la.getContent());
			array.add(o);
		}
		object.add("lastAnnoList", array);
		return object.toString();
	}

}
