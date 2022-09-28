package com.ssafy.sample.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssafy.sample.dto.Apt;
import com.ssafy.sample.dto.User;
import com.ssafy.sample.model.dao.AptDAOImpl;
import com.ssafy.sample.model.service.AptService;
import com.ssafy.sample.model.service.AptServiceImpl;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class AptController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AptService service = AptServiceImpl.getinstance();
	AptDAOImpl aptDao= AptDAOImpl.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("action>>>" + action);
		switch (action) {
		case "showApt":
			doList(request,response);
			break;
		case "news":
			doNews(request,response);
			break;
		case "likes":
			doLikes(request,response);
			break;


		}

	}// process



	



	private void doLikes(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			HttpSession session=request.getSession();
			User userinfo=(User)session.getAttribute("userinfo");
			List<String> likes=aptDao.likesList(userinfo);
			List<String> dongCodes=new ArrayList<>();
			
			
			for (String like: likes) {
				dongCodes.add(aptDao.convertCode(like));
			}
			request.setAttribute("likesList",dongCodes);
			
			
			RequestDispatcher dispatcher=request.getRequestDispatcher("/likes.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/main?action=error");
		}		
	}

	private void doNews(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			request.getRequestDispatcher("/news.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/main?action=error");
		}
		
	}

	private void doList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			String dong = request.getParameter("dong");

			request.setAttribute("list", aptDao.selectAll(dong));
			request.setAttribute("dongCode", dong);
			System.out.println(aptDao.selectAll(dong));
			request.getRequestDispatcher("/index.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			// error.jsp로 넘어가도록 구현할 수 있다.
			response.sendRedirect(request.getContextPath() + "/main?action=error");
		}
	}



}
