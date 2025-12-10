package bean;
import java.util.*;

import javax.persistence.EntityManager;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import businessLogic.BLFacade;
import domain.Ride;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;


@Named("createRideBean")
@SessionScoped
public class CreateRideBean implements Serializable {
private static final long serialVersionUID = 1L;
private String origin;
private String destination;
private Date date;
private int seats;
private float price;
private String mail;
private List<String> cities;

private String dateString;

private BLFacade facade = FacadeBean.getBusinessLogic();

private static final String DEFAULT_DRIVER_EMAIL = "driver1@gmail.com";

private static final SimpleDateFormat HTML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


public CreateRideBean(){
cities = facade.getDepartCities();
}


public String create() throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	if (date == null && dateString != null && !dateString.isEmpty()) {
		try {
			date = HTML_DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
		}
	}
	try {
		String driverEmail = (mail == null || mail.trim().isEmpty()) ? DEFAULT_DRIVER_EMAIL : mail.trim();
		Ride ride = facade.createAndStoreRide(origin, destination, date, seats, price, driverEmail);
		if (ride == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ride not created (driver may not exist)."));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ride created", "Ride saved successfully."));
		origin = null; destination = null; date = null; dateString = null; seats = 0; price = 0f; mail = null;
		return null; 
	} catch (Exception e) {
		e.printStackTrace();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
		return null;
	}
}




public String getOrigin(){return origin;}
public void setOrigin(String o){origin=o;}
public String getDestination(){return destination;}
public void setDestination(String d){destination=d;}
public Date getDate(){return date;}
public void setDate(Date d){date=d;}
public int getSeats(){return seats;}
public void setSeats(int s){seats=s;}
public List<String> getCities(){return cities;}
public float getPrice(){return price;}
public void setPrice(float p){price=p;}
public String getMail(){return mail;}
public void setMail(String m){mail=m;}

public String getDateString() {
	if (date != null) {
		return HTML_DATE_FORMAT.format(date);
	}
	return dateString;
}

public void setDateString(String ds) {
	this.dateString = ds;
	if (ds != null && !ds.isEmpty()) {
		try {
			this.date = HTML_DATE_FORMAT.parse(ds);
		} catch (ParseException e) {
			this.date = null;
		}
	} else {
		this.date = null;
	}
}
}