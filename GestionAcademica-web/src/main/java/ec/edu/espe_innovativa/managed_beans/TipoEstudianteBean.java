package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.TipoEstudiante;
import ec.edu.espe_innovativa.session_bean.TipoEstudianteFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "tipoEstudianteBean")
@ViewScoped
public class TipoEstudianteBean implements Serializable {

    @EJB
    private TipoEstudianteFacadeLocal tipoEstudianteFacadeLocal;
    private TipoEstudiante tipoEstudiante;
    private List<TipoEstudiante> tipoEstudianteList;

    public TipoEstudianteBean() {
    }

    @PostConstruct
    private void init() {
        tipoEstudiante=null;
        tipoEstudianteList=tipoEstudianteFacadeLocal.findAll();
    }

    public void nuevo() {
        tipoEstudiante = new TipoEstudiante();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (tipoEstudiante.getId() == null) {
                tipoEstudianteFacadeLocal.create(tipoEstudiante);
            } else {
                tipoEstudianteFacadeLocal.edit(tipoEstudiante);
            }
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe un Tipo de Estudiante con este nombre.");
                return;
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public void eliminar(TipoEstudiante tipoEstudiante) {
        try {
            tipoEstudianteFacadeLocal.remove(tipoEstudiante);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public TipoEstudiante getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public List<TipoEstudiante> getTipoEstudianteList() {
        return tipoEstudianteList;
    }

    public void setTipoEstudianteList(List<TipoEstudiante> tipoEstudianteList) {
        this.tipoEstudianteList = tipoEstudianteList;
    }

    
}
