package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.Modalidad;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.util.JasperReportUtil;
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

@Named(value = "reportesParaMailingBean")
@ViewScoped
public class ReportesParaMailingBean implements Serializable {

    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    private List<Curso> cursoList;
    private List<Canton> ciudadList;
    private List<Modalidad> modalidadList;

    private List<Curso> cursoListTemp;
    private List<Canton> ciudadListTemp;
    private List<Modalidad> modalidadListTemp;

    private List<Inscripcion> inscripcionList;

    private Integer tipoFiltro;  //1=curso, 2=ciudad, 3=modalidad

    public ReportesParaMailingBean() {
    }

    @PostConstruct
    public void init() {
        inscripcionList = inscripcionFacadeLocal.findAll();
        inscripcionList =inscripcionList.stream().filter(i->i.isInscripcionEstudiante()).collect(Collectors.toList());
        cursoList = new ArrayList<>();
        ciudadList = new ArrayList<>();
        modalidadList = new ArrayList<>();
        inscripcionList.stream().map(i -> i.getCursoCentroCapacitacion().getCurso()).distinct().forEach(c -> cursoList.add(c));
        inscripcionList.stream().map(i -> i.getCursoCentroCapacitacion().getCiudad()).distinct().forEach(c -> ciudadList.add(c));
        inscripcionList.stream().map(i -> i.getCursoCentroCapacitacion().getCurso().getModalidad()).distinct().forEach(m -> modalidadList.add(new Modalidad(String.valueOf(m))));
        
        cursoListTemp = new ArrayList<>();
        ciudadListTemp = new ArrayList<>();
        modalidadListTemp = new ArrayList<>();
        cursoList.forEach(c->cursoListTemp.add(new Curso(c.getId(), c.getNombre())));
        ciudadList.forEach(c->ciudadListTemp.add(new Canton(c.getId(), c.getCabeceraCantonal())));
        modalidadList.forEach(c->modalidadListTemp.add(new Modalidad(c.getCodigo())));
        
    }

    public void abrirVentanaFiltro(Integer tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
        if (tipoFiltro == 1) {///curso
            cursoListTemp.forEach(c -> c.setSeleccionado(false));
            cursoList.forEach((c) -> {
                for (Curso cursoTemp : cursoListTemp) {
                    if (cursoTemp.equals(c)) {
                        cursoTemp.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        } else if (tipoFiltro == 2) { //ciudad
            ciudadListTemp.forEach(c -> c.setSeleccionado(false));
            ciudadList.forEach((c) -> {
                for (Canton ciudadTemp : ciudadListTemp) {
                    if (ciudadTemp.equals(c)) {
                        ciudadTemp.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        } else {//modalidad
            modalidadListTemp.forEach(c -> c.setSeleccionado(false));
            modalidadList.forEach((c) -> {
                for (Modalidad modalidadTemp : modalidadListTemp) {
                    if (modalidadTemp.equals(c)) {
                        modalidadTemp.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        }

        PrimeFaces.current().executeScript("PF('dlgFiltro').show();");

    }

    public void aceptarFiltro() {
        if (tipoFiltro == 1) {///curso
            cursoList.forEach(c -> c.setSeleccionado(false));
            cursoListTemp.forEach((c) -> {
                for (Curso curso : cursoList) {
                    if (curso.equals(c)) {
                        curso.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        } else if (tipoFiltro == 2) { //ciudad
            ciudadList.forEach(c -> c.setSeleccionado(false));
            ciudadListTemp.forEach((c) -> {
                for (Canton ciudad : ciudadList) {
                    if (ciudad.equals(c)) {
                        ciudad.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        } else {//modalidad
            modalidadList.forEach(c -> c.setSeleccionado(false));
            modalidadListTemp.forEach((c) -> {
                for (Modalidad modalidad : modalidadList) {
                    if (modalidad.equals(c)) {
                        modalidad.setSeleccionado(c.isSeleccionado());
                        break;
                    }
                }
            });

        }
        PrimeFaces.current().executeScript("PF('dlgFiltro').hide();");

    }

    @Inject
    private JasperReportUtil jasperReportUtil;

    public void generarReporte(boolean exportarPdf) {
        List<Inscripcion> inscripcionListReporte = new ArrayList<>();
        for (Inscripcion i : inscripcionList) {
            if (verificarCurso(i) && verificarCiudad(i) && verificarModalidad(i)) {
                inscripcionListReporte.add(i);
            }
        }

        try {
            String nomReporte = "RepMailing";
            if (exportarPdf) {
                jasperReportUtil.exportToPdf(nomReporte, null, inscripcionListReporte);
            } else {
                jasperReportUtil.exportToXlsx(nomReporte, null, inscripcionListReporte);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesUtils.addErrorMessage("Error al generar reporte.");
        }
    }

    private boolean verificarCurso(Inscripcion i) {
        for (Curso curso : cursoList) {
            if (i.getCursoCentroCapacitacion().getCurso().equals(curso)) {
                return true;
            }
        }
        return false;
    }

    private boolean verificarCiudad(Inscripcion i) {
        for (Canton ciudad : ciudadList) {
            if (i.getCursoCentroCapacitacion().getCiudad().equals(ciudad)) {
                return true;
            }
        }
        return false;
    }

    private boolean verificarModalidad(Inscripcion i) {
        for (Modalidad modalidad : modalidadList) {
            if (i.getCursoCentroCapacitacion().getModalidad().toString().equals(modalidad.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public List<Curso> getCursoListTemp() {
        return cursoListTemp;
    }

    public void setCursoListTemp(List<Curso> cursoListTemp) {
        this.cursoListTemp = cursoListTemp;
    }

    public List<Canton> getCiudadListTemp() {
        return ciudadListTemp;
    }

    public void setCiudadListTemp(List<Canton> ciudadListTemp) {
        this.ciudadListTemp = ciudadListTemp;
    }

    public List<Modalidad> getModalidadListTemp() {
        return modalidadListTemp;
    }

    public void setModalidadListTemp(List<Modalidad> modalidadListTemp) {
        this.modalidadListTemp = modalidadListTemp;
    }

    public String getTituloFiltro() {
        if (null == tipoFiltro) {
            return "Filtro por Modalidad";
        } else switch (tipoFiltro) {
            case 1:
                return "Filtro por Curso";
            case 2:
                return "Filtro por Ciudad";
            default:
                return "Filtro por Modalidad";
        }

    }

    public Integer getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(Integer tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    
    
}
