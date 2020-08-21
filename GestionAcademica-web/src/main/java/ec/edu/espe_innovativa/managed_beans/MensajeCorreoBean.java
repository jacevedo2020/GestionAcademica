package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Opcion;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.TipoEstudiante;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.OpcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.RolFacadeLocal;
import ec.edu.espe_innovativa.session_bean.TipoEstudianteFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "mensajeCorreoBean")
@ViewScoped
public class MensajeCorreoBean implements Serializable {

    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    private MensajeCorreo mensajeCorreo;
    private List<MensajeCorreo> mensajeCorreoList;

    public MensajeCorreoBean() {
    }

    @PostConstruct
    private void init() {
        mensajeCorreo=null;
        mensajeCorreoList=mensajeCorreoFacadeLocal.findAll();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            mensajeCorreoFacadeLocal.edit(mensajeCorreo);
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(MensajeCorreo mensajeCorreo) {
        this.mensajeCorreo = mensajeCorreo;
    }

    public MensajeCorreo getMensajeCorreo() {
        return mensajeCorreo;
    }

    public void setMensajeCorreo(MensajeCorreo mensajeCorreo) {
        this.mensajeCorreo = mensajeCorreo;
    }

    public List<MensajeCorreo> getMensajeCorreoList() {
        return mensajeCorreoList;
    }

    public void setMensajeCorreoList(List<MensajeCorreo> mensajeCorreoList) {
        this.mensajeCorreoList = mensajeCorreoList;
    }
    
}
