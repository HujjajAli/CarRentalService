package com.rental.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rental.constants.ViewsConstants;
import com.rental.dao.CarDAO;
import com.rental.models.CarModel;
import com.rental.models.UserModel;


@WebServlet("/addEditCarServlet")
public class AddEditCarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CarDAO carDAO = new CarDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserModel userModel=(UserModel)request.getSession().getAttribute("user");
		String carId         = request.getParameter("carId");
        String carCompany    = request.getParameter("company");
        String carName       = request.getParameter("carName");
        String carModelStr   = request.getParameter("carModel");
        int    carYear       = Integer.parseInt(request.getParameter("carYear"));
        double price         = Double.parseDouble(request.getParameter("price"));
        
        
        CarModel carModel = new CarModel();
        carModel.setAppUserid(userModel.getAppUserid());
        carModel.setCarCompany(carCompany);
        carModel.setCarName(carName);
        carModel.setCarModel(carModelStr);
        carModel.setYear(carYear);
        carModel.setPrice(price);
        if(carId != null && !carId.equals("null") && Integer.parseInt(carId) > 0) {
        	
        	carModel.setCarId(Integer.parseInt(carId));
        	
        	boolean isInserted = carDAO.updateCar(carModel);
        	// Response
        	if (isInserted) {
        		request.setAttribute("clientCars", carDAO.getAllCarsByClient(userModel.getAppUserid()));
        		request.getRequestDispatcher(ViewsConstants.CLIENT_DASHBOARD_PAGE).forward(request, response);
        	} else {
        		response.getWriter().println("Failed to Update the Car. Please try again.");
        	}
        	
        }else {
        	boolean isInserted = carDAO.saveClientCar(carModel);
        	// Response
        	if (isInserted) {
        		request.setAttribute("clientCars", carDAO.getAllCarsByClient(userModel.getAppUserid()));
        		request.getRequestDispatcher(ViewsConstants.CLIENT_DASHBOARD_PAGE).forward(request, response);
        	} else {
        		response.getWriter().println("Failed to Save the Car. Please try again.");
        	}
        }
        
	}

}
