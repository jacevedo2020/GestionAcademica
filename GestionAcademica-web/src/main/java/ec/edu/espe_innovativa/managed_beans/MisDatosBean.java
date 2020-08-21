package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "misDatosBean")
@ViewScoped
public class MisDatosBean implements Serializable{

    private Provincia provincia;
    private Canton canton;
    @Inject
    private MenuBarBean menuBarBean;
    private Usuario usuario;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    
    public MisDatosBean() {
    }
    
    @PostConstruct
    public void init(){
        provincia=null;
        canton=null;
        usuario = usuarioFacadeLocal.find(menuBarBean.getUsuario().getId());
        if (usuario.getPersona().getParroquia()!=null){
            canton = usuario.getPersona().getParroquia().getCanton();
            provincia = usuario.getPersona().getParroquia().getCanton().getProvincia();
        }
    }
    
    public void grabar(){
        try {
            usuarioFacadeLocal.edit(usuario);
            menuBarBean.setUsuario(usuario);
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Date getFechaActual(){
        return new Date();
    }


}
