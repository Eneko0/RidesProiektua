package bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import domain.Driver;
import domain.User;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private User user = null;
    private String izena;
    private String pasahitza;

    private BLFacade facade = FacadeBean.getBusinessLogic();
    
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public String getPasahitza() { return pasahitza; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }

    public String login() {
        User erabiltzailea = facade.checkLogin(izena, pasahitza);
        if(erabiltzailea != null) {
        	this.user = erabiltzailea;
        	if (erabiltzailea.getMota().equals("Driver")) {
        		return "DriverPanel?faces-redirect=true";
        	} else {
        		return "TravelerPanel?faces-redirect=true";
        	}
            
        } else {
        	FacesContext.getCurrentInstance().addMessage(
	            null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Izena edo pasahitza ez da zuzena","Izena edo pasahitza ez da zuzena")
            );
            return null;
        }
    }
    
    public String logout() {
    	this.user = null;
    	return "Index?faces-redirect=true";
    }
    
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
