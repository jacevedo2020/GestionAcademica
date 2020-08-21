package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.util.JasperReportUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "reportesBean")
@ViewScoped
public class ReportesBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroFacadeLocal;
    @Inject
    private MenuBarBean menuBarBean;

    private List<CursoCentroCapacitacion> cursoCentroTodosList;
    private List<CursoCentroCapacitacion> cursoCentroList;

    private CursoCentroCapacitacion cursoCentro;
    private String tipoCurso;

    public ReportesBean() {
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
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)))
                .collect(Collectors.toList());
    }

    public void seleccionarCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
    }

    @Inject
    private JasperReportUtil jasperReportUtil;

    public void generarReporte(String nomReporte, boolean exportarPdf) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("curso.nombre", cursoCentro.getCurso().getNombre());
            params.put("instructor.persona.nombresCompletos", cursoCentro.getInstructor().getPersona().getNombresCompletos());
            params.put("instructor.persona.identificacion", cursoCentro.getInstructor().getPersona().getIdentificacion());
            params.put("fechaInicio", cursoCentro.getFechaInicio());
            params.put("fechaFin", cursoCentro.getFechaFin());
            params.put("horarioString", cursoCentro.getHorarioString());
            params.put("modalidad", cursoCentro.getModalidadDescripcion());
            params.put("noHoras", cursoCentro.getNroHoras());
            params.put("usuario", menuBarBean.getUsuario().getPersona().getNombresCompletos());
            String lugar;
            if (cursoCentro.isCursoContinuo()) {
                params.put("centroCapacitacion.administrador.nombres", cursoCentro.getCentroCapacitacion().getAdministradorActual().getAdministrador().getPersona().getNombresCompletos());
                params.put("proyecto.nombre", cursoCentro.getProyecto().getNombre());
                params.put("proyecto.numero", cursoCentro.getProyecto().getNumeroCompleto());
                lugar = cursoCentro.getCentroCapacitacion().getCanton().getCabeceraCantonal();
                params.put("curso.codigo", cursoCentro.getCurso().getCodigo());
                params.put("ciclo", cursoCentro.getCiclo());
            } else {
                params.put("centroCapacitacion.administrador.nombres", "");
                params.put("proyecto.nombre", "");
                params.put("proyecto.numero", "");
                lugar = cursoCentro.getCurso().getCanton().getCabeceraCantonal();
                params.put("ciclo", null);
            }
            params.put("ciudad", lugar);
            params.put("saldoPendienteCobro", BigDecimal.ZERO);
            params.put("totalPersonalParticular", cursoCentro.getTotalPersonalParticular());
            params.put("totalPersonalEgresadoESPE", cursoCentro.getTotalPersonalEgresadoESPE());
            params.put("totalPersonalEstudianteESPE", cursoCentro.getTotalPersonalEstudianteESPE());
            params.put("totalPersonalMilitaresFFAA", cursoCentro.getTotalPersonalMilitaresFFAA());
            params.put("totalPersonalServidorPublicoESPE", cursoCentro.getTotalPersonalServidorPublicoESPE());
            params.put("totalPersonalCapacidadesEspeciales", cursoCentro.getTotalPersonalCapacidadesEspeciales());
            params.put("totalPersonalCorporativo", cursoCentro.getTotalPersonalCorporativo());
            params.put("totalAprobados", cursoCentro.getTotalAprobados());
            params.put("totalReprobados", cursoCentro.getTotalReprobados());
            if (exportarPdf) {
                jasperReportUtil.exportToPdf(nomReporte, params, cursoCentro.getInscripcionEstudianteList());
            } else {
                jasperReportUtil.exportToXlsx(nomReporte, params, cursoCentro.getInscripcionEstudianteList());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesUtils.addErrorMessage("Error al generar reporte.");
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
        return cursoCentroList.stream().map(cc -> cc.getCurso().getPrograma().getNombre()).distinct().collect(Collectors.toList());
    }

    public List<String> getCursoFiltroList() {
        return cursoCentroList.stream().map(cc -> cc.getCurso().getNombre()).distinct().collect(Collectors.toList());
    }

    public List<String> getCentroCapacitacionFiltroList() {
        return cursoCentroList.stream().map(cc -> cc.getCentroCapacitacion().getNombre()).distinct().collect(Collectors.toList());
    }

}
