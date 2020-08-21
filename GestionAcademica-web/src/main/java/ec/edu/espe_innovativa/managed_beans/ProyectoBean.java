package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Proyecto;
import ec.edu.espe_innovativa.session_bean.ProyectoFacadeLocal;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proyectoBean")
@ViewScoped
public class ProyectoBean implements Serializable {

    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    private Proyecto proyecto;
    private List<Proyecto> proyectoList;

    public ProyectoBean() {
    }

    @PostConstruct
    private void init() {
        proyecto = null;
        proyectoList = proyectoFacadeLocal.findAll();
    }

    public void nuevo() {
        proyecto = new Proyecto();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            proyecto.setNombre(proyecto.getNombre().trim());
            if (proyecto.getId() == null) {
                proyectoFacadeLocal.create(proyecto);
            } else {
                proyectoFacadeLocal.edit(proyecto);
            }
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            try {
                if (e.getCause().getCause().getMessage().contains("(numero1, numero2, numero3)")) {
                    FacesUtils.addErrorMessage("formPrincipal:txtNumero2", "Ya existe un Proyecto con este número.");
                    return;
                }
                if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                    //FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe un Proyecto con este nombre.");
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un Proyecto con este nombre.", "Ya existe un Proyecto con este nombre."));
                }
            } catch (Exception e1) {
            }

            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void validarNombre(FacesContext fc, UIComponent c, Object v) throws ValidatorException {
        String nombreProyecto = ((String) v).trim();
        if (proyectoList != null && proyectoList.stream()
                .anyMatch(p -> {
                    if (proyecto.getId() == null) {
                        return p.getNombre().trim().equalsIgnoreCase(nombreProyecto);
                    } else {
                        return !p.getId().equals(proyecto.getId()) && p.getNombre().trim().equalsIgnoreCase(nombreProyecto);
                    }
                })) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un Proyecto con este nombre.", "Ya existe un Proyecto con este nombre."));
        }
    }

    public void seleccionar(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void eliminar(Proyecto proyecto) {
        try {
            proyectoFacadeLocal.remove(proyecto);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<Proyecto> getProyectoList() {
        return proyectoList;
    }

    public List<Proyecto> getProyectoActivoList() {
        return proyectoList.stream().filter(p -> p.getEstado().equals('E')).collect(Collectors.toList());
    }

    public void setProyectoList(List<Proyecto> proyectoList) {
        this.proyectoList = proyectoList;
    }

}
