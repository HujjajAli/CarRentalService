package com.rental.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rental.constants.UtilConstants;
import com.rental.constants.ViewsConstants;
import com.rental.dao.CarDAO;
import com.rental.dao.CustomerDAO;
import com.rental.dao.UserDAO;
import com.rental.models.CarModel;
import com.rental.models.UserModel;


@WebServlet("/loginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        UserModel userModel = userDAO.validateUser(username, password);

        if (userModel != null && userModel.getRole() != null) {
        	request.getSession().setAttribute("user", userModel);
            // Role-based forwarding
            switch (userModel.getRole()) {
                case UtilConstants.ADMIN_ROLE:
                    request.getRequestDispatcher(ViewsConstants.ADMIN_DASHBOARD_PAGE).forward(request, response);
                    break;
                case UtilConstants.CLIENT_ROLE:
                	request.setAttribute("clientCars", carDAO.getAllCarsByClient(userModel.getAppUserid()));
                    request.getRequestDispatcher(ViewsConstants.CLIENT_DASHBOARD_PAGE).forward(request, response);
                    break;
                case UtilConstants.CUSTOMER_ROLE:
                	List<CarModel> listOfCars =  carDAO.getAllCars();
                	listOfCars.forEach(e->{
                		UserModel fetchUserModel = userDAO.getUserById(e.getAppUserid());
                		e.setOwner(fetchUserModel.getName());
                	});
                	request.setAttribute("customerCars", listOfCars);
                    request.getRequestDispatcher(ViewsConstants.CUSTOMER_DASHBOARD_PAGE).forward(request, response);
                    break;
            }
        } else {
            // Invalid credentials
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher(ViewsConstants.LOGIN_PAGE).forward(request, response);
        }
	}

}
