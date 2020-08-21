package com.jvc.medisys.bean.segu;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named(value = "changePasswordBean")
@ViewScoped
public class ChangePasswordBean implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioServicio;

    private String username;
    private String password;
    private boolean claveCambiada;

    public ChangePasswordBean() {
    }

    @PostConstruct
    private void init() {
        try {
            //ssssssssssssss
            ///aaaaa
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioChangePassword").toString();
        } catch (Exception e) {
        }

    }

    public void cambiarContrasenia() {
        try {
            if (password.length() < 5) {
                FacesUtils.addErrorMessage("form1:pwd1", "La contraseña debe tener mínimo 5 caracteres.");
                return;
            }
            Usuario u = this.usuarioServicio.findByIdentificacion(username);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            u.setClave(passwordEncoder.encode(this.password));
            u.setClaveExpirada('N');
            this.usuarioServicio.edit(u);
            claveCambiada = true;            
        } catch (Exception e) {
            e.printStackTrace();
        }
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
