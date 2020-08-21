package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Evaluacion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import static java.io.File.separatorChar;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

@Named(value = "registroNotasBean")
@ViewScoped
public class RegistroNotasBean implements Serializable {

    @Inject
    private MenuBarBean menuBarBean;
    private CursoCentroCapacitacion cursoCentroCapacitacion;
    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroCapacitacionFacadeLocal;
    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    private boolean modoEdicion;
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionList;
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionTodosList;
    private String tipoCurso;
    private Inscripcion inscripcion;
    private BigDecimal nuevoValor;
    private String observacionModicacion;
    private boolean edicionEvaluacion;

    public RegistroNotasBean() {
    }

    @PostConstruct
    public void init() {
        /*try {
            PDDocument pDDocument = PDDocument.load(new File("C:\\StepUp\\Aplicaciones\\ImpresionCertificados\\CERT01.pdf"));
            PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
            PDField field = pDAcroForm.getField("fieldEstudiante");
            field.setValue("JOSE ACEVEDO MUÑOZ");
            pDDocument.save("C:\\StepUp\\Aplicaciones\\ImpresionCertificados\\CERT101.pdf");
            pDDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (final PDDocument document = PDDocument.load(new File("C:\\StepUp\\Aplicaciones\\ImpresionCertificados\\CERT101.pdf"))){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String fileName = "C:\\StepUp\\Aplicaciones\\ImpresionCertificados\\CERT101.png";
                ImageIOUtil.writeImage(bim, fileName, 300);
            }
            document.close();
        } catch (IOException e){
            e.printStackTrace();
        }
         */

        tipoCurso = Curso.TIPO_CONTINUO.toString();
        if (menuBarBean.getUsuario().verificarRolInstructor()) {
            menuBarBean.recargarUsuario();
        } else { //rol Analista, para aprobacion de notas
            cursoCentroCapacitacionTodosList = cursoCentroCapacitacionFacadeLocal.findAll();
        }
        modoEdicion = false;
        cursoCentroCapacitacion = null;
        tipoCursoChange();
    }

    public void grabar() {
        try {
            cursoCentroCapacitacion.actualizarEvaluacion(menuBarBean.getUsuario());
            cursoCentroCapacitacionFacadeLocal.edit(cursoCentroCapacitacion);
            cursoCentroCapacitacion = cursoCentroCapacitacionFacadeLocal.find(cursoCentroCapacitacion.getId());
            modoEdicion = false;
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void cancelar() {
        modoEdicion = false;
    }

    public void finalizar() {
        try {
            if (cursoCentroCapacitacion.isTieneInscripcionesEvaluacionPendienteDigitar()) {
                if (cursoCentroCapacitacion.getTipoCertificado().equals(Curso.TIPO_CERTIFICADO_APROBACION)) {
                    FacesUtils.addErrorMessage("No se ha ingresado el puntaje y asistencia para todos los estudiantes.");
                } else {
                    FacesUtils.addErrorMessage("No se ha ingresado la asistencia para todos los estudiantes.");
                }
                return;
            }
            cursoCentroCapacitacion.cambiarEstadoEvaluacionPendienteAprobacion();
            cursoCentroCapacitacionFacadeLocal.edit(cursoCentroCapacitacion);
            menuBarBean.recargarUsuario();
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void grabarAprobacionNotas() {
        try {
            cursoCentroCapacitacion.aprobarEvaluacion(menuBarBean.getUsuario());
            cursoCentroCapacitacionFacadeLocal.edit(cursoCentroCapacitacion);
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void tipoCursoChange() {
        List<CursoCentroCapacitacion> listaTemp;
        if (menuBarBean.getUsuario().verificarRolInstructor()) {
            listaTemp = menuBarBean.getUsuario().getCursosDictados();
        } else {//rol analista, para aprobacion de notas
            listaTemp = cursoCentroCapacitacionTodosList;
        }
        cursoCentroCapacitacionList = listaTemp.stream()
                .filter(cc -> cc.isTieneInscripcionesHabilitadasParaRegistroNota())
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)))
                .collect(Collectors.toList());
        /*cursoCentroCapacitacionList = listaTemp.stream()
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)))
                .filter(cc -> cc.isTieneInscripcionesEstadoPendienteAprobacion() || cc.isTieneInscripcionesEstadoAprobado())
                .collect(Collectors.toList());*/
    }

    public void abrirModificacionAsistencia(Inscripcion inscripcion) {
        edicionEvaluacion = true;
        this.inscripcion = inscripcion;
        nuevoValor = null;
        documentoJustificacionNombre = null;
        documentoJustificacionUrl = null;
        observacionModicacion = null;
        PrimeFaces.current().executeScript("PF('dlgModificacionAsistencia').show();");
    }

    public void abrirSolicitudRecalificacion(Inscripcion inscripcion) {
        edicionEvaluacion = true;
        this.inscripcion = inscripcion;
        documentoJustificacionNombre = null;
        documentoJustificacionUrl = null;
        observacionModicacion = null;
        PrimeFaces.current().executeScript("PF('dlgSolicitudRecalificacionPuntaje').show();");
    }

    public void abrirSolicitudRecalificacionEjecutada(Inscripcion inscripcion) {
        edicionEvaluacion = false;
        this.inscripcion = inscripcion;
        documentoJustificacionNombre = null;
        documentoJustificacionUrl = null;
        observacionModicacion = null;
        PrimeFaces.current().executeScript("PF('dlgSolicitudRecalificacionPuntaje').show();");
    }

    public void abrirVisualizacionAsistencia(Inscripcion inscripcion) {
        edicionEvaluacion = false;
        this.inscripcion = inscripcion;
        PrimeFaces.current().executeScript("PF('dlgModificacionAsistencia').show();");
    }
    private String documentoJustificacionNombre;
    private String documentoJustificacionUrl;

    private void subirDocumentoJustificacion(FileUploadEvent event, Integer idEvaluacion) throws IOException {
        String pathRelativo = idEvaluacion.toString();
        String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoModificacionEvaluacion() + separatorChar + pathRelativo;

        if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
            Files.createDirectories(Paths.get(pathAbsoluto));
        }

        String nombreDocumento = Utils.depurarNombreDocumento(event.getFile().getFileName());
        pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;
        Files.copy(event.getFile().getInputstream(), Paths.get(pathAbsoluto), StandardCopyOption.REPLACE_EXISTING);

        documentoJustificacionNombre = nombreDocumento;
        documentoJustificacionUrl = ParametrosGenerales.URL_MODIFICACION_EVALUACION + "/" + pathRelativo;

    }

    public void subirDocumentoJustificacionAsistencia(FileUploadEvent event) {
        try {
            Evaluacion evaluacion = inscripcion.getEvaluacionAsistenciaInicial();
            subirDocumentoJustificacion(event, evaluacion.getId());
        } catch (Exception e) {
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
    }

    public void subirDocumentoJustificacionPuntaje(FileUploadEvent event) {
        try {
            Evaluacion evaluacion = inscripcion.getEvaluacionPuntajeInicial();
            subirDocumentoJustificacion(event, evaluacion.getId());
        } catch (Exception e) {
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
    }

    public void grabarModificacionAsistencia() {
        try {

            inscripcion.setAsistencia(nuevoValor);
            //actualizar la evaluacioni inicial ingresada por el Instructor
            Evaluacion evaluacionInicial = inscripcion.getEvaluacionAsistenciaInicial();
            evaluacionInicial.setDocumentoJustificacionNombre(documentoJustificacionNombre);
            evaluacionInicial.setDocumentoJustificacionUrl(documentoJustificacionUrl);
            evaluacionInicial.setModificacionObservacion(observacionModicacion);

            //actualizar o crear la evaluacion modificada por el Analista
            Evaluacion evaluacionModificada = inscripcion.getEvaluacionAsistenciaModificada();
            if (evaluacionModificada != null) {
                evaluacionModificada.setValor(nuevoValor);
                evaluacionModificada.setFecha(new Date());
                evaluacionModificada.setCreadoPor(menuBarBean.getUsuario());
            } else {
                inscripcion.agregarEvaluacion(Evaluacion.TIPO_VALOR_ASISTENCIA, menuBarBean.getUsuario());
            }
            inscripcionFacadeLocal.edit(inscripcion);
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
            edicionEvaluacion = false;
            cursoCentroCapacitacion = cursoCentroCapacitacionFacadeLocal.find(cursoCentroCapacitacion.getId());

            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void grabarSolicitudRecalificacion() {
        try {

            inscripcion.setEstadoEvaluacion(Inscripcion.ESTADO_EVALUACION_SOLICITADO_RECALIFICACION);
            //actualizar la evaluacioni inicial ingresada por el Instructor
            Evaluacion evaluacionInicial = inscripcion.getEvaluacionPuntajeInicial();
            evaluacionInicial.setDocumentoJustificacionNombre(documentoJustificacionNombre);
            evaluacionInicial.setDocumentoJustificacionUrl(documentoJustificacionUrl);
            evaluacionInicial.setModificacionObservacion(observacionModicacion);
            evaluacionInicial.setSolicitudModificacionFecha(new Date());
            evaluacionInicial.setSolicitudModificacionPor(menuBarBean.getUsuario());

            inscripcionFacadeLocal.edit(inscripcion);
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
            FacesUtils.addMessageRegistroGrabado();
            edicionEvaluacion = false;
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
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

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
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

    public BigDecimal getNuevoValor() {
        return nuevoValor;
    }

    public void setNuevoValor(BigDecimal nuevoValor) {
        this.nuevoValor = nuevoValor;
    }

    public String getDocumentoJustificacionNombre() {
        return documentoJustificacionNombre;
    }

    public void setDocumentoJustificacionNombre(String documentoJustificacionNombre) {
        this.documentoJustificacionNombre = documentoJustificacionNombre;
    }

    public String getDocumentoJustificacionUrl() {
        return documentoJustificacionUrl;
    }

    public void setDocumentoJustificacionUrl(String documentoJustificacionUrl) {
        this.documentoJustificacionUrl = documentoJustificacionUrl;
    }

    public String getDocumentoJustificacionUrlCompleto() {
        try {
            return this.documentoJustificacionUrl + "/" + this.documentoJustificacionNombre;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isEdicionEvaluacion() {
        return edicionEvaluacion;
    }

    public void setEdicionEvaluacion(boolean edicionEvaluacion) {
        this.edicionEvaluacion = edicionEvaluacion;
    }

    public String getObservacionModicacion() {
        return observacionModicacion;
    }

    public void setObservacionModicacion(String observacionModicacion) {
        this.observacionModicacion = observacionModicacion;
    }

}
