package com.ourdream.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ourdream.service.LastAnnouncementService;

@WebServlet("/SendMessageToClient")
public class SendMessageToClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendMessageToClient() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		response.setContentType("application/json;charset=UTF-8");
		if ("lastAnnouncement".equals(method)) {
			doLastAnno(response);
		}

	}

	private void doLastAnno(HttpServletResponse response) throws IOException {
		LastAnnouncementService las = new LastAnnouncementService();
		try {
			String json = las.sendJson();
			response.getWriter().write(json);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
