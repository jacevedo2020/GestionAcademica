package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Programa;
import ec.edu.espe_innovativa.entity_bean.TipoEstudiante;
import ec.edu.espe_innovativa.session_bean.ProgramaFacadeLocal;
import ec.edu.espe_innovativa.session_bean.TipoEstudianteFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "programaBean")
@ViewScoped
public class ProgramaBean implements Serializable {

    @EJB
    private ProgramaFacadeLocal programaFacadeLocal;
    private Programa programa;
    private List<Programa> programaList;

    public ProgramaBean() {
    }

    @PostConstruct
    private void init() {
        programa=null;
        programaList=programaFacadeLocal.findAll();
    }

    public void nuevo() {
        programa = new Programa();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            programa.setNombre(programa.getNombre().trim());
            if (programa.getId() == null) {
                programaFacadeLocal.create(programa);
            } else {
                programaFacadeLocal.edit(programa);
            }
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains("(codigo)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtCodigo", "Ya existe un Área de Conocimiento con este número.");
                return;
            }
            if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe un Área de Conocimiento con este nombre.");
                return;
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(Programa programa) {
        this.programa = programa;
    }

    public void eliminar(Programa programa) {
        try {
            programaFacadeLocal.remove(programa);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public List<Programa> getProgramaList() {
        return programaList;
    }
    public List<Programa> getProgramaActivoList() {
        if (programaList==null)
            return new ArrayList<>();
        return programaList.stream().filter(p -> p.getEstado().equals('A')).collect(Collectors.toList());
    }

    public void setProgramaList(List<Programa> programaList) {
        this.programaList = programaList;
    }

    
    
}
