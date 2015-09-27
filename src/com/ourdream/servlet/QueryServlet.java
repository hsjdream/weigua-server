package com.ourdream.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ourdream.beans.Cet;
import com.ourdream.service.BusTimetableService;
import com.ourdream.service.CetService;

/**
 * 查询功能的入口
 * 
 * @author kylin
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public QueryServlet() {
		super();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request, response);
	}

	/**
	 * doGet method 实现对不同请求的不同处理 以及字符编码
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		response.setContentType("application/json;charset=UTF-8");
		String method = request.getParameter("method");
		try {
			if ("cet".equals(method)) {
				doCet(request, response);
			} else if ("bus".equals(method)) {
				doBus(request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doBus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BusTimetableService busService = new BusTimetableService();
		String json = busService.sendJson();
		response.getWriter().write(json);
	}

	/**
	 * 将Cet的查询结果以json的方式返回给客户端
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void doCet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		// 测试数据
		// String id = "610021151109301";
		// String name = "刘帆";
		Cet cet = new Cet();
		cet.setId(id);
		cet.setName(name);
		CetService cetService = new CetService(cet);
		String json = cetService.sendJson();
		response.getWriter().write(json);
	}
}
