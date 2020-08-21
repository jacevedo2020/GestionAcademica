package com.jvc.medisys.bean.segu;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Named
@ViewScoped
public class LoginBean implements Serializable {
    
    private String username;
    private String password;
    private boolean claveCambiada;

    @PostConstruct
    private void init() {
        try {
            //FacesUtils.getExternalContext().invalidateSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() throws ServletException, IOException {
        MenuBarBean menuBarBean = ((MenuBarBean) FacesUtils.getManagedBean("menuBar"));
        if (menuBarBean != null) {
            try {
                menuBarBean.inicializar(username);
            } catch (Exception e) {
            }
        }

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("usuarioChangePassword", username);
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/login");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String logout() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/logout");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();

        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isClaveCambiada() {
        return claveCambiada;
    }

    public void setClaveCambiada(boolean claveCambiada) {
        this.claveCambiada = claveCambiada;
    }


}
