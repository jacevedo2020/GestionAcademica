package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.AdministradorCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.CentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

@Named(value = "centroCapacitacionBean")
@ViewScoped
public class CentroCapacitacionBean implements Serializable {

    @EJB
    private CentroCapacitacionFacadeLocal centroCapacitacionFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private CentroCapacitacion centroCapacitacion;
    private List<CentroCapacitacion> centroCapacitacionList;
    private List<Usuario> administradorCentroList;
    private AdministradorCentroCapacitacion administradorCentroCapacitacion;
    @Inject
    private MenuBarBean menuBarBean;
    public CentroCapacitacionBean() {
    }

    @PostConstruct
    private void init() {
        administradorCentroList = usuarioFacadeLocal.findByRol(Rol.ROL_ADMINISTRADOR_CENTRO);
        initCentroCapacitacion();
    }

    private void initCentroCapacitacion() {
        centroCapacitacion = null;
        centroCapacitacionList = centroCapacitacionFacadeLocal.findAll();

    }

    public void nuevo() {
        centroCapacitacion = new CentroCapacitacion();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (centroCapacitacion.getId() == null) {
                centroCapacitacionFacadeLocal.create(centroCapacitacion);
            } else {
                centroCapacitacionFacadeLocal.edit(centroCapacitacion);
            }
            initCentroCapacitacion();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe un Centro de Capacitación con este nombre.");
                return;
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(CentroCapacitacion centroCapacitacion) {
        this.centroCapacitacion = centroCapacitacion;
    }

    public void eliminar(CentroCapacitacion centroCapacitacion) {
        try {
            centroCapacitacionFacadeLocal.remove(centroCapacitacion);
            initCentroCapacitacion();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public CentroCapacitacion getCentroCapacitacion() {
        return centroCapacitacion;
    }

    public void setCentroCapacitacion(CentroCapacitacion centroCapacitacion) {
        this.centroCapacitacion = centroCapacitacion;
    }

    public List<CentroCapacitacion> getCentroCapacitacionList() {
        return centroCapacitacionList;
    }

    public void setCentroCapacitacionList(List<CentroCapacitacion> centroCapacitacionList) {
        this.centroCapacitacionList = centroCapacitacionList;
    }

    public void nuevoAdministrador() {
        administradorCentroCapacitacion = new AdministradorCentroCapacitacion();

    }

    public List<Usuario> getAdministradorCentroList() {
        if (centroCapacitacion == null) {
            return new ArrayList<>();
        }
        return administradorCentroList.stream().filter(o -> !centroCapacitacion.verificarAdministradorAsignado(o)).collect(Collectors.toList());
    }

    public void setAdministradorCentroList(List<Usuario> administradorCentroList) {
        this.administradorCentroList = administradorCentroList;
    }

    public AdministradorCentroCapacitacion getAdministradorCentroCapacitacion() {
        return administradorCentroCapacitacion;
    }

    public void setAdministradorCentroCapacitacion(AdministradorCentroCapacitacion administradorCentroCapacitacion) {
        this.administradorCentroCapacitacion = administradorCentroCapacitacion;
    }

    public void aceptarAdministrador() {
        administradorCentroCapacitacion.setCreadoPor(menuBarBean.getUsuario());
        centroCapacitacion.agregarAdministrador(administradorCentroCapacitacion);
        PrimeFaces.current().executeScript("PF('dlgUsuariosAdminCentro').hide();");
    }

}
