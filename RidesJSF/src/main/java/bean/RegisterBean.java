package bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import domain.Driver;

@Named("registerBean")
@RequestScoped
public class RegisterBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String izena;
    private String email;
    private String pasahitza;

    private BLFacade facade = FacadeBean.getBusinessLogic();

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasahitza() { return pasahitza; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }

    public String erregistratu() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            Driver d = facade.register(izena, email, pasahitza);
            if (d != null) {
                return "Login?faces-redirect=true";
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea", "Errorea"));
                return null;
            }
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea","Errorea"));
            return null;
        }
    }
}
