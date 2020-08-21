package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "misInscripcionesBean")
@ViewScoped
public class MisInscripcionesBean implements Serializable {

    private Inscripcion inscripcion;
    @Inject
    private MenuBarBean menuBarBean;
    public MisInscripcionesBean() {
    }

    @PostConstruct
    public void init() {
        inscripcion = null;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }


    
}
