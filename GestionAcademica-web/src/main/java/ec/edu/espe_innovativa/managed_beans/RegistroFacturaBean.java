package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Estado;
import ec.edu.espe_innovativa.entity_bean.EstadoInscripcion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
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

@Named(value = "registroFacturaBean")
@ViewScoped
public class RegistroFacturaBean implements Serializable {

    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    private List<Inscripcion> inscripcionList;
    private List<Inscripcion> inscripcionGrupoList;
    private Inscripcion inscripcion;
    private Inscripcion inscripcionEstudiante;
    @Inject
    private MenuBarBean menuBarBean;
    private EstadoInscripcion estadoInscripcion;
    private List<Estado> estadoList;

    public RegistroFacturaBean() {
    }

    @PostConstruct
    public void init() {
        inscripcion = null;
        inscripcionList = inscripcionFacadeLocal.findAll();
        inscripcionList = inscripcionList
                .stream()
                .filter(i -> i.getInscripcionPadre() == null)
                .collect(Collectors.toList());
    }

    public void seleccionarInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
        if (this.inscripcion.isInscripcionEstudianteGrupo()) {
            inscripcionGrupoList = this.inscripcion.getCursoCentroCapacitacion().getInscripcionEstudianteList()
                    .stream()
                    .filter(i -> i.getInscripcionPadre() != null && i.getInscripcionPadre().equals(this.inscripcion))
                    .collect(Collectors.toList());
        }
    }

    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public List<Inscripcion> getInscripcionGrupoList() {
        return inscripcionGrupoList;
    }

    public void setInscripcionGrupoList(List<Inscripcion> inscripcionGrupoList) {
        this.inscripcionGrupoList = inscripcionGrupoList;
    }

    public Inscripcion getInscripcionEstudiante() {
        return inscripcionEstudiante;
    }

    public void setInscripcionEstudiante(Inscripcion inscripcionEstudiante) {
        this.inscripcionEstudiante = inscripcionEstudiante;
    }

    public void abrirEstudianteGrupo(Inscripcion inscripcion) {
        inscripcionEstudiante = inscripcion;
        PrimeFaces.current().executeScript("PF('dlgEstudianteGrupo').show();");
    }

    public void inicioCambiarEstado() {
        estadoInscripcion = new EstadoInscripcion();
        estadoList = new ArrayList<>();
        if (menuBarBean.getUsuario().verificarRolAsistenteFacturacion()) {
            Estado estado = new Estado();
            estado.setId(Estado.ID_ESTADO_FACTURA_REGISTRADA);
            estado.setDescripcion("Factura Registrada");
            estadoList.add(estado);
        } else {//Financiero
            Estado estado = new Estado();
            estado.setId(Estado.ID_ESTADO_FACTURA_LIQUIDADA);
            estado.setDescripcion("Factura Liquidada");
            estadoList.add(estado);

            estado = new Estado();
            estado.setId(Estado.ID_ESTADO_MATRICULA_APROBADA);
            estado.setDescripcion("Matrícula Aprobada");
            estadoList.add(estado);
        }
        PrimeFaces.current().executeScript("PF('dlgEstado').show();");
    }

    public EstadoInscripcion getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(EstadoInscripcion estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

    public List<Estado> getEstadoList() {
        return estadoList;
    }

    public void setEstadoList(List<Estado> estadoList) {
        this.estadoList = estadoList;
    }

    public void confirmarCambioEstado() {
        try {
            if (inscripcion.getCursoCentroCapacitacion().getInscripcionEmpresa() == null) {
                estadoInscripcion.setUsuario(menuBarBean.getUsuario());
                inscripcion.agregarEstado(estadoInscripcion);
                if (menuBarBean.getUsuario().verificarRolAsistenteFacturacion()) {
                    inscripcion.setFacturaNumero(estadoInscripcion.getFacturaNumero());
                }
                inscripcionFacadeLocal.edit(inscripcion);

                if (inscripcion.isInscripcionEstudianteGrupo()) {
                    for (Inscripcion i : inscripcionGrupoList) {
                        EstadoInscripcion est = new EstadoInscripcion();
                        est.setUsuario(menuBarBean.getUsuario());
                        est.setEstado(estadoInscripcion.getEstado());
                        est.setObservacion(estadoInscripcion.getObservacion());
                        if (menuBarBean.getUsuario().verificarRolAsistenteFacturacion()) {
                            est.setFacturaNumero(estadoInscripcion.getFacturaNumero());
                            i.setFacturaNumero(estadoInscripcion.getFacturaNumero());
                        }
                        i.agregarEstado(est);
                        inscripcionFacadeLocal.edit(i);
                    }
                }
            } else {//inscripcion institucional
                for (Inscripcion i : inscripcion.getCursoCentroCapacitacion().getInscripcionList()) {
                    EstadoInscripcion est = new EstadoInscripcion();
                    est.setUsuario(menuBarBean.getUsuario());
                    est.setEstado(estadoInscripcion.getEstado());
                    est.setObservacion(estadoInscripcion.getObservacion());
                    if (menuBarBean.getUsuario().verificarRolAsistenteFacturacion()) {
                        est.setFacturaNumero(estadoInscripcion.getFacturaNumero());
                        i.setFacturaNumero(estadoInscripcion.getFacturaNumero());
                    }
                    i.agregarEstado(est);
                    inscripcionFacadeLocal.edit(i);
                }
            }
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
            PrimeFaces.current().executeScript("PF('dlgEstado').hide();");
            PrimeFaces.current().ajax().update("formPrincipal:msgPrincipal", "formPrincipal:panelPrincipal");
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

}
