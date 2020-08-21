package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Estado;
import ec.edu.espe_innovativa.entity_bean.EstadoInscripcion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.util.JasperReportUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

@Named(value = "aprobacionInscripcionBean")
@ViewScoped
public class AprobacionInscripcionBean implements Serializable {

    @Inject
    private MenuBarBean menuBarBean;
    private CursoCentroCapacitacion cursoCentroCapacitacion;
    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroCapacitacionFacadeLocal;
    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    @Inject
    private JasperReportUtil jasperReportUtil;

    private List<CursoCentroCapacitacion> cursoCentroCapacitacionList;
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionTodosList;
    private String tipoCurso;
    private Inscripcion inscripcion;
    private Inscripcion inscripcionEstudiante;
    private EstadoInscripcion estadoInscripcion;
    private List<Estado> estadoList;
    private List<Inscripcion> inscripcionList;
    private boolean pagadoTemp;

    public AprobacionInscripcionBean() {
    }

    @PostConstruct
    public void init() {
        tipoCurso = Curso.TIPO_CONTINUO.toString();
        cursoCentroCapacitacionTodosList = cursoCentroCapacitacionFacadeLocal.findAll();
        cursoCentroCapacitacion = null;
        tipoCursoChange();
    }

    public void grabar() {
        try {

            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void cancelar() {
    }

    public void tipoCursoChange() {
        cursoCentroCapacitacionList = cursoCentroCapacitacionTodosList.stream()
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)))
                .collect(Collectors.toList());
        if (tipoCurso.equals(Curso.TIPO_CONTINUO.toString())) {
            cursoCentroCapacitacionList = cursoCentroCapacitacionList.stream()
                    .filter(cc -> cc.getProyecto().getResponsable().equals(menuBarBean.getUsuario()))
                    .collect(Collectors.toList());
        } else {    //corporativos
            cursoCentroCapacitacionList = cursoCentroCapacitacionList.stream()
                    .filter(cc -> cc.getResponsable().equals(menuBarBean.getUsuario()))
                    .collect(Collectors.toList());
        }

    }

    public CursoCentroCapacitacion getCursoCentroCapacitacion() {
        return cursoCentroCapacitacion;
    }

    public void setCursoCentroCapacitacion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        this.cursoCentroCapacitacion = cursoCentroCapacitacion;

    }

    public void seleccionarCursoCentroCapacitacion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        this.cursoCentroCapacitacion = cursoCentroCapacitacion;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public List<CursoCentroCapacitacion> getCursoCentroCapacitacionList() {
        return cursoCentroCapacitacionList;
    }

    public void setCursoCentroCapacitacionList(List<CursoCentroCapacitacion> cursoCentroCapacitacionList) {
        this.cursoCentroCapacitacionList = cursoCentroCapacitacionList;
    }

    public List<String> getProgramaFiltroList() {
        return cursoCentroCapacitacionList.stream()
                .map(cc -> cc.getCurso().getPrograma().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCursoFiltroList() {
        return cursoCentroCapacitacionList.stream()
                .map(cc -> cc.getCurso().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCentroCapacitacionFiltroList() {
        return cursoCentroCapacitacionList.stream()
                .map(cc -> cc.getCentroCapacitacion().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public void seleccionarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getInscripcionPadre() == null) {
            this.inscripcion = inscripcion;
        } else {
            this.inscripcion = inscripcion.getInscripcionPadre();
            inscripcionList = cursoCentroCapacitacion.getInscripcionEstudianteList()
                    .stream()
                    .filter(cc -> cc.getInscripcionPadre() != null && cc.getInscripcionPadre().equals(this.inscripcion))
                    .collect(Collectors.toList());
        }
    }

    public void inicioCambiarEstado() {
        estadoInscripcion = new EstadoInscripcion();
        estadoList = new ArrayList<>();
        if (menuBarBean.getUsuario().verificarRolAnalista()) {
            Estado estado = new Estado();
            estado.setId(Estado.ID_ESTADO_MATRICULA_APROBADA);
            estado.setDescripcion("Matrícula Aprobada");
            estadoList.add(estado);
            estado = new Estado();
            estado.setId(Estado.ID_ESTADO_MATRICULA_ANULADA);
            estado.setDescripcion("Matrícula Anulada");
            estadoList.add(estado);
        }
        PrimeFaces.current().executeScript("PF('dlgEstado').show();");
    }

    public void inicioConfirmacionPago() {
        pagadoTemp = inscripcion.isPagadoSi();
        PrimeFaces.current().executeScript("PF('dlgConfirmacionPagado').show();");
    }

    public List<Estado> getEstadoList() {
        return estadoList;
    }

    public void setEstadoList(List<Estado> estadoList) {
        this.estadoList = estadoList;
    }

    public EstadoInscripcion getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(EstadoInscripcion estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

    public void confirmarCambioEstado() {
        try {
            if (cursoCentroCapacitacion.getInscripcionEmpresa() == null) {
                estadoInscripcion.setUsuario(menuBarBean.getUsuario());
                inscripcion.agregarEstado(estadoInscripcion);
                inscripcionFacadeLocal.edit(inscripcion);

                if (inscripcion.isInscripcionEstudianteGrupo()) {
                    for (Inscripcion i : inscripcionList) {
                        EstadoInscripcion est = new EstadoInscripcion();
                        est.setUsuario(menuBarBean.getUsuario());
                        est.setEstado(estadoInscripcion.getEstado());
                        est.setObservacion(estadoInscripcion.getObservacion());
                        i.agregarEstado(est);
                        inscripcionFacadeLocal.edit(i);
                    }
                }
            } else {//inscripcion institucional
                for (Inscripcion i : cursoCentroCapacitacion.getInscripcionList()) {
                    EstadoInscripcion est = new EstadoInscripcion();
                    est.setUsuario(menuBarBean.getUsuario());
                    est.setEstado(estadoInscripcion.getEstado());
                    est.setObservacion(estadoInscripcion.getObservacion());
                    i.agregarEstado(est);
                    inscripcionFacadeLocal.edit(i);
                }
                cursoCentroCapacitacion = cursoCentroCapacitacionFacadeLocal.find(cursoCentroCapacitacion.getId());
            }
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
            PrimeFaces.current().executeScript("PF('dlgEstado').hide();");
            PrimeFaces.current().ajax().update("formPrincipal:msgPrincipal", "formPrincipal:panelPrincipal");
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void confirmarPago() {
        try {
            inscripcion.setPagado(pagadoTemp ? 'S' : 'N');
            inscripcionFacadeLocal.edit(inscripcion);
            if (cursoCentroCapacitacion.getInscripcionEmpresa() == null) {
                if (inscripcion.isInscripcionEstudianteGrupo()) {
                    for (Inscripcion i : inscripcionList) {
                        i.setPagado(pagadoTemp ? 'S' : 'N');
                        inscripcionFacadeLocal.edit(i);
                    }
                }
            } else {//inscripcion institucional
                for (Inscripcion i : cursoCentroCapacitacion.getInscripcionList()) {
                    i.setPagado(pagadoTemp ? 'S' : 'N');
                    inscripcionFacadeLocal.edit(i);
                }
                cursoCentroCapacitacion = cursoCentroCapacitacionFacadeLocal.find(cursoCentroCapacitacion.getId());
            }
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
            PrimeFaces.current().executeScript("PF('dlgConfirmacionPagado').hide();");
            PrimeFaces.current().ajax().update("formPrincipal:msgPrincipal", "formPrincipal:panelPrincipal");
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void retornarListadoInscripciones() {
        cursoCentroCapacitacion = cursoCentroCapacitacionFacadeLocal.find(cursoCentroCapacitacion.getId());
        inscripcion = null;
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

    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public boolean isPagadoTemp() {
        return pagadoTemp;
    }

    public void setPagadoTemp(boolean pagadoTemp) {
        this.pagadoTemp = pagadoTemp;
    }

    public void generarReporte(Inscripcion inscripcion, boolean exportarPdf) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", inscripcion.getId());
            params.put("curso.nombre", inscripcion.getCursoCentroCapacitacion().getCurso().getNombre());
            params.put("curso.modalidad", inscripcion.getCursoCentroCapacitacion().getModalidad());
            params.put("fecha", inscripcion.getFecha());
            params.put("curso.fechaInicio", inscripcion.getCursoCentroCapacitacion().getFechaInicio());
            params.put("curso.horarioString", inscripcion.getCursoCentroCapacitacion().getHorarioString());
            params.put("estudiante.apellidosNombres", inscripcion.getEstudiante().getPersona().getNombresCompletos());
            params.put("estudiante.identificacion", inscripcion.getEstudiante().getPersona().getIdentificacion());
            params.put("estudiante.direccion", inscripcion.getEstudiante().getPersona().getDireccion());
            if (inscripcion.getEstudiante().getPersona().getParroquia() != null) {
                params.put("estudiante.provincia", inscripcion.getEstudiante().getPersona().getParroquia().getCanton().getProvincia().getNombre());
                params.put("estudiante.canton", inscripcion.getEstudiante().getPersona().getParroquia().getCanton().getNombre());
                params.put("estudiante.parroquia", inscripcion.getEstudiante().getPersona().getParroquia().getNombre());
            }
            params.put("estudiante.direccionReferencia", inscripcion.getEstudiante().getPersona().getDireccionReferencia());
            params.put("estudiante.contactoEmergencia", inscripcion.getEstudiante().getPersona().getDatosContacto());
            params.put("estudiante.correo", inscripcion.getEstudiante().getPersona().getEmail());
            params.put("estudiante.telefono", inscripcion.getEstudiante().getPersona().getTelefono());
            params.put("estudiante.celular", inscripcion.getEstudiante().getPersona().getCelular());
            params.put("estudiante.genero", inscripcion.getEstudiante().getPersona().getGenero());
            params.put("estudiante.edad", inscripcion.getEdadEstudiante());
            params.put("estudiante.trabajaActualmente", inscripcion.getTrabajaActualmente());
            params.put("estudiante.empresaTrabajo", inscripcion.getEmpresaTrabajo());
            params.put("estudiante.direccionTrabajo", inscripcion.getDireccionTrabajo());
            params.put("estudiante.telefonoTrabajo", inscripcion.getTelefonoTrabajo());
            if (inscripcion.getInscripcionPadre() == null) {
                params.put("formaPago", inscripcion.getFormaPago());
            } else {
                params.put("formaPago", inscripcion.getInscripcionPadre().getFormaPago());
            }
            if (inscripcion.getInscripcionPadre() == null) {
                params.put("medioEntero", inscripcion.getMedioEnteroCurso());
            } else {
                params.put("medioEntero", inscripcion.getInscripcionPadre().getMedioEnteroCurso());
            }
            if (inscripcion.getInscripcionPadre() == null) {
                params.put("facturaRazonSocial", inscripcion.getFacturaRazonSocial());
                params.put("facturaDireccion", inscripcion.getFacturaDireccion());
                params.put("facturaEmail", inscripcion.getFacturaEmail());
                params.put("facturaRuc", inscripcion.getFacturaRuc());
                params.put("facturaTelefono", inscripcion.getFacturaTelefono());

            } else {
                params.put("facturaRazonSocial", inscripcion.getInscripcionPadre().getFacturaRazonSocial());
                params.put("facturaDireccion", inscripcion.getInscripcionPadre().getFacturaDireccion());
                params.put("facturaEmail", inscripcion.getInscripcionPadre().getFacturaEmail());
                params.put("facturaRuc", inscripcion.getInscripcionPadre().getFacturaRuc());
                params.put("facturaTelefono", inscripcion.getInscripcionPadre().getFacturaTelefono());

            }

            if (exportarPdf) {
                jasperReportUtil.exportToPdf("RepMatriculaIndividual", params);
            } else {
                jasperReportUtil.exportToXlsx("RepMatriculaIndividual", params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesUtils.addErrorMessage("Error al generar reporte.");
        }
    }
    public void generarReporteInstitucional(CursoCentroCapacitacion cursoCentroCapacitacion, boolean exportarPdf) {
        try {
            Inscripcion inscripcion = cursoCentroCapacitacion.getInscripcionEmpresa();
            
            Map<String, Object> params = new HashMap<>();
            params.put("id", inscripcion.getId());
            params.put("curso.nombre", inscripcion.getCursoCentroCapacitacion().getCurso().getNombre());
            params.put("curso.modalidad", inscripcion.getCursoCentroCapacitacion().getModalidad());
            params.put("fecha", inscripcion.getFecha());
            params.put("curso.horarioString", inscripcion.getCursoCentroCapacitacion().getHorarioString());
            params.put("estudiante.contactoEmergencia", inscripcion.getEstudiante().getPersona().getContactoNombres());
            params.put("estudiante.direccion", inscripcion.getEstudiante().getPersona().getDireccion());
            if (inscripcion.getEstudiante().getPersona().getParroquia() != null) {
                params.put("estudiante.provincia", inscripcion.getEstudiante().getPersona().getParroquia().getCanton().getProvincia().getNombre());
                params.put("estudiante.canton", inscripcion.getEstudiante().getPersona().getParroquia().getCanton().getNombre());
                params.put("estudiante.parroquia", inscripcion.getEstudiante().getPersona().getParroquia().getNombre());
            }
            params.put("estudiante.direccionReferencia", inscripcion.getEstudiante().getPersona().getDireccionReferencia());
            params.put("estudiante.correo", inscripcion.getEstudiante().getPersona().getEmail());
            params.put("estudiante.telefono", inscripcion.getEstudiante().getPersona().getContactoTelefono());
            params.put("medioEntero", inscripcion.getMedioEnteroCurso());
            params.put("facturaRazonSocial", inscripcion.getFacturaRazonSocial());
            params.put("facturaDireccion", inscripcion.getFacturaDireccion());
            params.put("facturaEmail", inscripcion.getFacturaEmail());
            params.put("facturaRuc", inscripcion.getFacturaRuc());
            params.put("facturaTelefono", inscripcion.getFacturaTelefono());

            if (exportarPdf) {
                jasperReportUtil.exportToPdf("RepMatriculaInstitucional", params, cursoCentroCapacitacion.getInscripcionEstudianteList());
            } else {
                jasperReportUtil.exportToXlsx("RepMatriculaInstitucional", params, cursoCentroCapacitacion.getInscripcionEstudianteList());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesUtils.addErrorMessage("Error al generar reporte.");
        }
    }

}
