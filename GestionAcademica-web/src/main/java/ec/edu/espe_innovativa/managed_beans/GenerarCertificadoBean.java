package ec.edu.espe_innovativa.managed_beans;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Certificado;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Evaluacion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.Parametro;
import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import ec.edu.espe_innovativa.session_bean.CertificadoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.ParametroFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.io.File.separatorChar;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

@Named(value = "generarCertificadoBean")
@ViewScoped
public class GenerarCertificadoBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroFacadeLocal;
    @EJB
    private ParametroFacadeLocal parametroFacadeLocal;
    @EJB
    private CertificadoFacadeLocal certificadoFacadeLocal;
    private Parametro parametroSecuencialCertificados;
    @Inject
    private MenuBarBean menuBarBean;

    private List<CursoCentroCapacitacion> cursoCentroTodosList;
    private List<CursoCentroCapacitacion> cursoCentroList;
    private CursoCentroCapacitacion cursoCentro;
    private String tipoCurso;
    private PlantillaCertificado plantilla;
    private Date fechaParaImpresion;
    private String urlCertificadoPreview;
    private Integer idCertificado;
    private boolean seleccionarTodos;
    private Inscripcion inscripcion;
    private Certificado certificado;

    public GenerarCertificadoBean() {
    }

    @PostConstruct
    public void init() {
        tipoCurso = Curso.TIPO_CONTINUO.toString();
        cursoCentroTodosList = cursoCentroFacadeLocal.findAll();
        cursoCentro = null;
        tipoCursoChange();
    }

    public void tipoCursoChange() {
        cursoCentroList = cursoCentroTodosList.stream()
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)) && cc.isEvaluacionAprobada())
                .collect(Collectors.toList());
    }

    public void seleccionarCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
        seleccionarTodos = false;

    }

    public void abrirGenerarCertificados() {
        boolean encontro = false;
        fechaParaImpresion = new Date();
        for (Inscripcion i : cursoCentro.getInscripcionHabilitadaParaGenerarCertificadoList()) {
            if (i.isSeleccionado()) {
                encontro = true;
                break;
            }
        }
        if (!encontro) {
            FacesUtils.addErrorMessage("Debe seleccionar al menos un estudiante.");
            return;
        }
        plantilla = null;
        urlCertificadoPreview = null;
        parametroSecuencialCertificados = parametroFacadeLocal.find(Parametro.ID_SECUENCIAL_CERTIFICADO);
        idCertificado = certificadoFacadeLocal.getMaximoId() + 1;
        PrimeFaces.current().executeScript("PF('dlgGenerarCertificados').show();");
    }

    public void abrirSubirCertificado(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
        idCertificado = certificadoFacadeLocal.getMaximoId() + 1;
        certificado = new Certificado();
        certificado.setId(idCertificado);
        certificado.setInscripcion(inscripcion);
        certificado.setCreadoPor(menuBarBean.getUsuario());
        urlCertificadoPreview = null;
        PrimeFaces.current().executeScript("PF('dlgSubirCertificado').show();");
    }

    public void generar() {
        try {
            for (Inscripcion insc : cursoCentro.getInscripcionHabilitadaParaGenerarCertificadoList()) {
                if (insc.isSeleccionado()) {
                    Certificado cert = new Certificado();
                    cert.setFechaParaImpresion(fechaParaImpresion);
                    cert.setId(idCertificado);
                    idCertificado++;
                    Integer secuencial = Integer.valueOf(parametroSecuencialCertificados.getValor());
                    cert.setSecuencial(secuencial);
                    parametroSecuencialCertificados.setValor((++secuencial).toString());
                    cert.setCreadoPor(menuBarBean.getUsuario());
                    generarCertificado(insc, cert);
                    cert.setInscripcion(insc);
                    certificadoFacadeLocal.create(cert);
                    parametroFacadeLocal.edit(parametroSecuencialCertificados);
                }
            }
            cursoCentro = cursoCentroFacadeLocal.find(cursoCentro.getId());
            seleccionarTodos = false;
            FacesUtils.addInfoMessage("Certificados generados exitosamente.");
            PrimeFaces.current().executeScript("PF('dlgGenerarCertificados').hide();");
            PrimeFaces.current().ajax().update("formPrincipal:msgPrincipal", "formPrincipal:dtCalificaciones");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocurrió un error inesperado al generar los certificados.");
        }
    }

    /*private void generarCertificado(Inscripcion insc, Certificado cert, boolean convertirImagen) throws Exception {
        PDDocument pDDocument = getDocumentoPlantilla();
        PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
        PDField field = null;
        try {
            field = pDAcroForm.getField("txtEstudiante");
            field.setValue(insc.getEstudiante().getPersona().getNombresCompletos());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtEstudianteCedula");
            field.setValue(insc.getEstudiante().getPersona().getNombresCompletosConCedula());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtCurso");
            field.setValue(insc.getCursoCentroCapacitacion().getCurso().getNombre());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtHoras");
            field.setValue(insc.getCursoCentroCapacitacion().getNroHoras().toString());
        } catch (Exception e) {
        }
        //System.out.println(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(new Date()));
        try {
            field = pDAcroForm.getField("txtFechasRango");
            Calendar calFechaInicio= Calendar.getInstance();
            Calendar calFechaFin= Calendar.getInstance();
            calFechaInicio.setTime(insc.getCursoCentroCapacitacion().getFechaInicio());
            calFechaFin.setTime(insc.getCursoCentroCapacitacion().getFechaFin());
            String rangoFechas = new SimpleDateFormat("'del' dd 'de' MMMMM", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio()); 
            if (calFechaInicio.get(Calendar.YEAR)!=calFechaFin.get(Calendar.YEAR)){
                rangoFechas += new SimpleDateFormat(" 'de' MMMMM", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio()); 
            }
            rangoFechas += new SimpleDateFormat(" 'al' dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaFin()); 
            field.setValue(rangoFechas);
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaInicio");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio()));
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaFin");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaFin()));
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaActual");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(new Date()));
        } catch (Exception e) {
        }

        String pathRelativo = cert.getId().toString();
        String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoCertificados() + separatorChar + pathRelativo;

        if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
            Files.createDirectories(Paths.get(pathAbsoluto));
        }
        String nombreDocumento = "cert-" + cert.getSecuencial() + ".pdf";
        pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;
        pDDocument.save(pathAbsoluto);
        //pDDocument.close();

        //Convertir el certificado generado en pdf a jpg
        if (convertirImagen) {
            pathAbsoluto = pathAbsoluto.replaceAll(".pdf", ".png");
            nombreDocumento = nombreDocumento.replaceAll(".pdf", ".png");
            PDFRenderer pdfRenderer = new PDFRenderer(pDDocument);
            //for (int page = 0; page < pDDocument.getNumberOfPages(); ++page) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            ImageIOUtil.writeImage(bufferedImage, pathAbsoluto, 300);
            //}
        }
        pDDocument.close();

        cert.setDocumentoNombre(nombreDocumento);
        cert.setDocumentoUrl(ParametrosGenerales.URL_CERTIFICADOS + "/" + pathRelativo);
    }
    /
    /*private PdfDocument getDocumentoPlantilla() throws IOException {
        String pathRelativo = plantilla.getId().toString();
        String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoPlantillas() + separatorChar + pathRelativo;
        String nombreDocumento = plantilla.getDocumentoPlantillaNombre();
        pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;

        PdfDocument pDDocument = PdfDocument.load(new File(pathAbsoluto));
        return pDDocument;
    }*/
    private void generarCertificado(Inscripcion insc, Certificado cert) throws Exception {
        String pathRelativo = cert.getId().toString();
        String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoCertificados() + separatorChar + pathRelativo;

        if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
            Files.createDirectories(Paths.get(pathAbsoluto));
        }
        String nombreDocumento = "cert-" + cert.getSecuencial() + ".pdf";
        pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;

        PdfDocument pdfDocument = getDocumentoPlantilla(pathAbsoluto);
        PdfAcroForm pDAcroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
        PdfFormField field = null;
        try {
            field = pDAcroForm.getField("txtSecuencial");
            field.setValue(cert.getSecuencial().toString());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtCedula");
            field.setValue(insc.getEstudiante().getPersona().getIdentificacion());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtNombre");
            field.setValue(insc.getEstudiante().getPersona().getNombreParaCertificado());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtNombreCedula");
            field.setValue(insc.getEstudiante().getPersona().getNombreParaCertificado()+ " - " + insc.getEstudiante().getPersona().getIdentificacion());        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtCurso");
            field.setValue(insc.getCursoCentroCapacitacion().getCurso().getNombre());
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtHoras");
            field.setValue(insc.getCursoCentroCapacitacion().getNroHoras().toString());
        } catch (Exception e) {
        }
        //System.out.println(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(new Date()));
        try {
            field = pDAcroForm.getField("txtFechasRango");
            Calendar calFechaInicio = Calendar.getInstance();
            Calendar calFechaFin = Calendar.getInstance();
            calFechaInicio.setTime(insc.getCursoCentroCapacitacion().getFechaInicio());
            calFechaFin.setTime(insc.getCursoCentroCapacitacion().getFechaFin());
            String rangoFechas = new SimpleDateFormat("'del' dd 'de' MMMMM", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio());
            if (calFechaInicio.get(Calendar.YEAR) != calFechaFin.get(Calendar.YEAR)) {
                rangoFechas += new SimpleDateFormat(" 'de' MMMMM", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio());
            }
            rangoFechas += new SimpleDateFormat(" 'al' dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaFin());
            field.setValue(rangoFechas);
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaInicio");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaInicio()));
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaFin");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(insc.getCursoCentroCapacitacion().getFechaFin()));
        } catch (Exception e) {
        }
        try {
            field = pDAcroForm.getField("txtFechaImpresion");
            field.setValue(new SimpleDateFormat("dd 'de' MMMMM 'del' yyyy", new Locale("es", "EC")).format(cert.getFechaParaImpresion()));
        } catch (Exception e) {
        }

        //pdfDocument.save(pathAbsoluto);
        //pDDocument.close();
        pdfDocument.close();

        //Convertir el certificado generado en pdf a jpg
        PDFRenderer pdfRenderer = new PDFRenderer(PDDocument.load(new FileInputStream(pathAbsoluto)));
        pathAbsoluto = pathAbsoluto.replaceAll(".pdf", ".png");
        nombreDocumento = nombreDocumento.replaceAll(".pdf", ".png");
        //for (int page = 0; page < pDDocument.getNumberOfPages(); ++page) {
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        ImageIOUtil.writeImage(bufferedImage, pathAbsoluto, 300);
        //}
        //pDDocument.close();
        cert.setDocumentoNombre(nombreDocumento);
        cert.setDocumentoUrl(ParametrosGenerales.URL_CERTIFICADOS + "/" + pathRelativo);
    }

    private PdfDocument getDocumentoPlantilla(String pathDestino) throws IOException {
        String pathRelativo = plantilla.getId().toString();
        String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoPlantillas() + separatorChar + pathRelativo;
        String nombreDocumento = plantilla.getDocumentoPlantillaNombre();
        pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;

        PdfDocument pDDocument = new PdfDocument(new PdfReader(pathAbsoluto), new PdfWriter(pathDestino));
        return pDDocument;
    }

    public void generarVistaPrevia() {
        try {
            Inscripcion insc = getInscripcionEjemplo();
            Certificado cert = new Certificado();
            cert.setFechaParaImpresion(fechaParaImpresion);
            cert.setId(idCertificado);
            Integer secuencial = Integer.valueOf(parametroSecuencialCertificados.getValor());
            cert.setSecuencial(secuencial);

            generarCertificado(insc, cert);
            urlCertificadoPreview = cert.getDocumentoUrl() + "/" + cert.getDocumentoNombre();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocurrió un error al generar la vista previa del certificado.");
        }
    }

    public void seleccionarTodosChange() {
        for (Inscripcion i : cursoCentro.getInscripcionHabilitadaParaGenerarCertificadoList()) {
            if (i.isAlumnoApruebaCursoConfirmado()) {
                i.setSeleccionado(seleccionarTodos);
            }
        }
    }

    public void seleccionarChange() {
        seleccionarTodos = true;
        for (Inscripcion i : cursoCentro.getInscripcionHabilitadaParaGenerarCertificadoList()) {
            if (i.isAlumnoApruebaCursoConfirmado() && !i.isSeleccionado()) {
                seleccionarTodos = false;
                break;
            }
        }
    }

    private Inscripcion getInscripcionEjemplo() {
        for (Inscripcion i : cursoCentro.getInscripcionHabilitadaParaGenerarCertificadoList()) {
            if (i.isSeleccionado()) {
                return i;
            }
        }
        return null;
    }

    public void grabarSubirCertificado() {
        try {
            certificadoFacadeLocal.create(certificado);
            cursoCentro = cursoCentroFacadeLocal.find(cursoCentro.getId());
            FacesUtils.addInfoMessage("Certificado subido exitosamente.");
            PrimeFaces.current().executeScript("PF('dlgSubirCertificado').hide();");
            PrimeFaces.current().ajax().update("formPrincipal:msgPrincipal", "formPrincipal:dtCalificaciones");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocurrió un error inesperado al subir el certificado.");
        }

    }

    public List<CursoCentroCapacitacion> getCursoCentroList() {
        return cursoCentroList;
    }

    public void setCursoCentroList(List<CursoCentroCapacitacion> cursoCentroList) {
        this.cursoCentroList = cursoCentroList;
    }

    public CursoCentroCapacitacion getCursoCentro() {
        return cursoCentro;
    }

    public void setCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public List<String> getProgramaFiltroList() {
        return cursoCentroList.stream()
                .map(cc -> cc.getCurso().getPrograma().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCursoFiltroList() {
        return cursoCentroList.stream()
                .map(cc -> cc.getCurso().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCentroCapacitacionFiltroList() {
        return cursoCentroList.stream()
                .map(cc -> cc.getCentroCapacitacion().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public PlantillaCertificado getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(PlantillaCertificado plantilla) {
        this.plantilla = plantilla;
    }

    public String getUrlCertificadoPreview() {
        return urlCertificadoPreview;
    }

    public void setUrlCertificadoPreview(String urlCertificadoPreview) {
        this.urlCertificadoPreview = urlCertificadoPreview;
    }

    public boolean isSeleccionarTodos() {
        return seleccionarTodos;
    }

    public void setSeleccionarTodos(boolean seleccionarTodos) {
        this.seleccionarTodos = seleccionarTodos;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public Date getFechaParaImpresion() {
        return fechaParaImpresion;
    }

    public void setFechaParaImpresion(Date fechaParaImpresion) {
        this.fechaParaImpresion = fechaParaImpresion;
    }
    
    

    public void subirDocumentoCertificado(FileUploadEvent event) {
        try {

            String pathRelativo = certificado.getId().toString();
            String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoCertificados() + separatorChar + pathRelativo;

            if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
                Files.createDirectories(Paths.get(pathAbsoluto));
            }
            String nombreDocumento = Utils.depurarNombreDocumento(event.getFile().getFileName());
            pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;
            Files.copy(event.getFile().getInputstream(), Paths.get(pathAbsoluto), StandardCopyOption.REPLACE_EXISTING);

            certificado.setDocumentoNombre(nombreDocumento);
            certificado.setDocumentoUrl(ParametrosGenerales.URL_CERTIFICADOS + "/" + pathRelativo);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
    }

}
