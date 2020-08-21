package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Opcion;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.OpcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.RolFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "rolBean")
@ViewScoped
public class RolBean implements Serializable {

    @EJB
    private RolFacadeLocal rolFacadeLocal;
    @EJB
    private OpcionFacadeLocal opcionFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private Rol rol;
    private List<Rol> rolesList;
    private List<Opcion> opcionList;
    private List<Opcion> opcionSeleccionList;
    private List<Usuario> usuarioList;
    private List<Usuario> usuarioSeleccionList;

    public RolBean() {
    }

    @PostConstruct
    private void init() {
        opcionList = opcionFacadeLocal.findAll();
        usuarioList = usuarioFacadeLocal.findAll();
        opcionList = opcionList.stream().filter(o -> o.getOpcionList() == null || o.getOpcionList().isEmpty()).collect(Collectors.toList());
        initRoles();
    }

    private void initRoles() {
        rolesList = rolFacadeLocal.findAll();
        rol = null;
    }

    public void nuevo() {
        rol = new Rol();
    }

    public void nuevoOpcion() {
        opcionSeleccionList = opcionList.stream().filter(o -> !rol.verificarOpcionAsignada(o)).collect(Collectors.toList());
        opcionSeleccionList.forEach(o -> o.setSeleccionado(false));
        PrimeFaces.current().executeScript("PF('dlgOpciones').show();");
    }

    public void nuevoUsuario() {
        usuarioSeleccionList = usuarioList.stream().filter(o -> !rol.verificarUsuarioAsignado(o)).collect(Collectors.toList());
        usuarioSeleccionList.forEach(o -> o.setSeleccionado(false));
        PrimeFaces.current().executeScript("PF('dlgUsuarios').show();");
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (rol.getId() == null) {
                rolFacadeLocal.create(rol);
            } else {
                rolFacadeLocal.edit(rol);
            }
            initRoles();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe un Rol con este nombre.");
                return;
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(Rol rol) {
        this.rol = rol;
    }

    public void eliminar(Rol rol) {
        try {
            rolFacadeLocal.remove(rol);
            initRoles();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Rol> rolesList) {
        this.rolesList = rolesList;
    }

    public List<Usuario> getUsuarioSeleccionList() {
        return usuarioSeleccionList;
    }

    public void setUsuarioSeleccionList(List<Usuario> usuarioSeleccionList) {
        this.usuarioSeleccionList = usuarioSeleccionList;
    }

    public List<Opcion> getOpcionSeleccionList() {
        return opcionSeleccionList;
    }

    public void setOpcionSeleccionList(List<Opcion> opcionSeleccionList) {
        this.opcionSeleccionList = opcionSeleccionList;
    }

    public void agregarOpciones() {
        rol.agregarOpciones(opcionSeleccionList.stream().filter(o -> o.isSeleccionado()).collect(Collectors.toList()));
        PrimeFaces.current().executeScript("PF('dlgOpciones').hide();");

    }

    public void agregarUsuarios() {
        rol.agregarUsuarios(usuarioSeleccionList.stream().filter(o -> o.isSeleccionado()).collect(Collectors.toList()));
        PrimeFaces.current().executeScript("PF('dlgUsuarios').hide();");

    }
}
