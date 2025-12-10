package bean;

import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped; 

import businessLogic.BLFacade;
import domain.Ride;

@Named("quearyRideBean")
@SessionScoped
public class QueryRideBean implements Serializable {
    private static final long serialVersionUID = 1L;
	private String origin;
    private List<String> originList;
    private String destination;
    private List<String> destinationList;
    private Date date;
    private String dateString;
    private List<Ride> results = new ArrayList<>();

    private BLFacade facade = FacadeBean.getBusinessLogic();
    private static final SimpleDateFormat HTML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public QueryRideBean() {
    }

    @PostConstruct
    public void init() {
        originList = facade.getDepartCities();
        if (!originList.isEmpty()) {
            origin = originList.get(0);
            destinationList = facade.getDestinationCities(origin);
            if (!destinationList.isEmpty()) {
                destination = destinationList.get(0);
            }
        } else {
            destinationList = new ArrayList<>();
        }
    }
    
    public String search() {
        if (dateString != null && !dateString.isEmpty()) {
            try {
                date = HTML_DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                date = null;
            }
        } else {
            date = null;
        }

        try {
            results = facade.getRides(origin, destination, date);
        } catch (Exception e) {
            results = new ArrayList<>();
        }
        return null; 
    }

    public String getOrigin() { return origin; }
    public List<String> getOriginList() { 
    	this.originList = facade.getDepartCities();
    	return this.originList; 
    }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public List<String> getDestinationList() {
    	this.destinationList = facade.getDestinationCities(this.origin);
    	return this.destinationList; 
    }
    public void setDestination(String destination) { this.destination = destination; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDateString() { 
        if (date != null) return HTML_DATE_FORMAT.format(date);
        return dateString;
    }
    public void setDateString(String dateString) { 
        this.dateString = dateString; 
        if (dateString != null && !dateString.isEmpty()) {
            try { this.date = HTML_DATE_FORMAT.parse(dateString); } catch (ParseException e) { this.date = null; }
        } else this.date = null;
    }
    
    public void originChanged() {
        if (origin != null) {
            destinationList = facade.getDestinationCities(origin);
        } else {
            destinationList = new ArrayList<>();
        }
        destination = null;
    }


    public List<Ride> getResults() { return results; }
}
