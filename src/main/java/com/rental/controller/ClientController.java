package com.rental.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rental.constants.UtilConstants;
import com.rental.constants.ViewsConstants;
import com.rental.dao.CarDAO;
import com.rental.models.CarModel;
import com.rental.models.CarNameModel;
import com.rental.models.CarNameVariantModel;
import com.rental.models.CompanyModel;


@WebServlet("/clientController")
public class ClientController extends HttpServlet {
	
	private static final String ACTION_ADD_NEW_CAR = "addNewCar";
	private static final String ACTION_UPDATE_CAR = "updateCar";
	
	private static final long serialVersionUID = 1L;
       
    
    public ClientController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter(UtilConstants.ACTION_PARAMETER);
		CarDAO carDAO = new CarDAO();
		switch (action) {
			case ACTION_ADD_NEW_CAR:
				List<CompanyModel> listOfComapnies = carDAO.getAllCompanies();
				String companyName = "";
				String carName     = "";
				if(listOfComapnies != null && listOfComapnies.size() > 0)
					companyName = listOfComapnies.get(0).getCompanyName();
				List<CarNameModel> listOfCarNameModel = carDAO.getAllCarNamesByCompany(companyName);
				if(listOfCarNameModel != null && listOfCarNameModel.size() > 0)
					 carName = listOfCarNameModel.get(0).getCarName();
				List<CarNameVariantModel> listOfCarNameVariantModel = carDAO.getAllCarNameModelsByCarName(carName);
				
				request.setAttribute("companies", listOfComapnies);
				request.setAttribute("carNames", listOfCarNameModel);
				request.setAttribute("carModelVariants", listOfCarNameVariantModel);
	            request.getRequestDispatcher(ViewsConstants.ADD_CAR_FORM).forward(request, response);
	            break;
	       case ACTION_UPDATE_CAR:
	    	   String carId = request.getParameter("carId");
	    	   request.setAttribute("carId",carId);
	    	   CarModel carModel = carDAO.getCarByCarId(Long.parseLong(carId));
	    	   listOfComapnies = carDAO.getAllCompanies();
	    	   String selComp = listOfComapnies
	    			    .stream()
	    			    .filter(e -> e.getCompanyName().equals(carModel.getCarCompany()))
	    			    .collect(Collectors.toList()).get(0).getCompanyName();
			   companyName = "";
			   carName     = "";
				if(listOfComapnies != null && listOfComapnies.size() > 0) {
					companyName = selComp;
				}
				listOfCarNameModel = carDAO.getAllCarNamesByCompany(companyName);
				List<CarNameModel> listOfCarNameModelCopy = listOfCarNameModel
	    			    .stream()
	    			    .filter(e -> e.getCarName().equals(carModel.getCarName()))
	    			    .collect(Collectors.toList());
				String selCarNames = listOfCarNameModelCopy.get(0).getCarName();
				if(listOfCarNameModel != null && listOfCarNameModel.size() > 0) {
					 carName = selCarNames;
				}
				listOfCarNameVariantModel = carDAO.getAllCarNameModelsByCarName(carName);
				String selCarModelVariants = listOfCarNameVariantModel
	    			    .stream()
	    			    .filter(e -> e.getModel().equals(carModel.getCarModel()))
	    			    .collect(Collectors.toList()).get(0).getModel();
				
				request.setAttribute("companies", listOfComapnies);
				request.setAttribute("carNames", listOfCarNameModel);
				request.setAttribute("carModelVariants", listOfCarNameVariantModel);
				
				request.setAttribute("selComp", selComp);
				request.setAttribute("selCarNames", selCarNames);
				request.setAttribute("selCarModelVariants", selCarModelVariants);
				request.setAttribute("price", carModel.getPrice());
				request.setAttribute("year", carModel.getYear());
				
				
	            request.getRequestDispatcher("updateCarForm.jsp").forward(request, response);
	            break;
		}
	}

}
