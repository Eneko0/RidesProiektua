package bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import domain.Driver;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Driver driver = null;
    private String izena;
    private String pasahitza;

    private BLFacade facade = FacadeBean.getBusinessLogic();
    
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public String getPasahitza() { return pasahitza; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }

    public String login() {
        Driver erabiltzailea = facade.checkLogin(izena, pasahitza);
        if(erabiltzailea != null) {
        	driver = erabiltzailea;
            return "DriverPanel?faces-redirect=true";
        } else {
        	FacesContext.getCurrentInstance().addMessage(
	            null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Izena edo pasahitza ez da zuzena","Izena edo pasahitza ez da zuzena")
            );
            return null;
        }
    }
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}
