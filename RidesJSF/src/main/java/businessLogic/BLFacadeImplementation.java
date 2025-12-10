package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new HibernateDataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartCities(){
		
		 List<String> departLocations=dbManager.getDepartCities();		

		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getDestinationCities(String from){
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
//   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
//	   
//		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
//		return ride;
//   };
	
   /**
    * {@inheritDoc}
    */
 
	public List<Ride> getRides(String from, String to, Date date){
		List<Ride>  rides=dbManager.getRides(from, to, date);
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}
	
	public Driver checkLogin(String name, String password) {
		Driver driver=dbManager.checkLogin(name, password);
		return driver;
	}
	
	public Driver register(String name, String email, String password) {
		Driver driver=dbManager.register(name, email ,password);
		return driver;
	}
	
	public void close() {
		HibernateDataAccess dB4oManager=new HibernateDataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
	 public void initializeBD(){
		dbManager.initializeDB();
	}
	 
	 public Ride createAndStoreRide(String origin, String destination, Date date, int seats, float price, String driverEmail) {
		 Ride ride=dbManager.createAndStoreRide(origin, destination, date, seats, price, driverEmail);		
		 return ride;
	 }

}

