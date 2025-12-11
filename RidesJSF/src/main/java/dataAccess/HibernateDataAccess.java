package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import configuration.UtilDate;

import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {

	public HibernateDataAccess() {
	}

	public void initializeDB() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez", "123");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga", "123");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver", "123");

			em.persist(driver1);
			em.persist(driver2);
			em.persist(driver3);

			em.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public List<String> getDepartCities() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from",
					String.class);
			return query.getResultList();
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public List<String> getArrivalCities(String from) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
					String.class);
			query.setParameter(1, from);
			return query.getResultList();
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public Ride createAndStoreRide(String origin, String destination, Date Data, int seats, float price,
			String driverEmail) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Ride u = new Ride();
			u.setFrom(origin);
			u.setTo(destination);
			u.setDate(Data);
			u.setBetMinimum(seats);
			u.setPrice(price);
			em.persist(u);
			em.getTransaction().commit();
			return u;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public List<Ride> getRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);
			TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
					Ride.class);
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, date);
			return new ArrayList<Ride>(query.getResultList());
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			System.out.println(">> DataAccess: getEventsMonth");
			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			TypedQuery<Date> query = em.createQuery(
					"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
					Date.class);
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			return new ArrayList<Date>(query.getResultList());
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public User checkLogin(String name, String password) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        TypedQuery<User> query = em.createQuery(
	            "SELECT u FROM User u WHERE u.name = :name AND u.password = :password", User.class);
	        query.setParameter("name", name);
	        query.setParameter("password", password);
	        List<User> userList = query.getResultList();
	        return userList.isEmpty() ? null : userList.get(0);
	    } finally {
	        if (em.isOpen())
	            em.close();
	    }
	}


	public User register(String name, String email, String password, String mota) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<User> query = em.createQuery("SELECT r FROM User r WHERE r.name=?1 OR r.email=?2",
					User.class);
			query.setParameter(1, name);
			query.setParameter(2, email);
			List<User> userList = query.getResultList();

			if (userList.isEmpty()) {
				if (mota.equals("Driver")) {
					User dri = new Driver(email, name, password);
					em.persist(dri);
					em.getTransaction().commit();
					return (dri);
				} else if (mota.equals("Traveler")) {
					User trv = new Traveler(email, name, password);
					em.persist(trv);
					em.getTransaction().commit();
					return (trv);
				} else {
					return (null);
				}
				
			} else {
				em.getTransaction().commit();
				return (null);
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			return (null);
		} finally {
			if (em.isOpen())
				em.close();
		}
	}

	public void close() {
		JPAUtil.close();
		System.out.println("DataAcess closed");
	}
}
