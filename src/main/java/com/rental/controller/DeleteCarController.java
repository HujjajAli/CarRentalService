package com.rental.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rental.constants.ViewsConstants;
import com.rental.dao.CarDAO;
import com.rental.dao.UserDAO;
import com.rental.models.CarModel;
import com.rental.models.UserModel;

/**
 * Servlet implementation class DeleteCarController
 */
@WebServlet("/deleteCarController")
public class DeleteCarController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCarController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDAO userDAO = new UserDAO();
		CarDAO carDAO = new CarDAO();
		long carId = Long.parseLong(request.getParameter("carId"));
		carDAO.deleteClientCarById(carId);
		UserModel userModel=(UserModel)request.getSession().getAttribute("user");
		request.setAttribute("clientCars", carDAO.getAllCarsByClient(userModel.getAppUserid()));
		request.getRequestDispatcher(ViewsConstants.CLIENT_DASHBOARD_PAGE).forward(request, response);
	}

}
