package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.session_bean.CentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "cursoCentroCapacitacionBean")
@ViewScoped
public class CursoCentroCapacitacionBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroFacadeLocal;
    @EJB
    private CentroCapacitacionFacadeLocal centroCapacitacionFacadeLocal;
    private CursoCentroCapacitacion cursoCentro;
    private List<CursoCentroCapacitacion> cursoCentroList;
    @Inject
    private MenuBarBean menuBarBean;
    
    public CursoCentroCapacitacionBean() {
    }

    @PostConstruct
    public void init() {
        initCursoCentro();
    }

    private void initCursoCentro() {
        cursoCentro = null;
        cursoCentroList = cursoCentroFacadeLocal.findAll();
        cursoCentroList = cursoCentroList.stream()
                .filter(cc->cc.isCursoContinuo())
                .collect(Collectors.toList());
    }

    public void nuevo() {
        cursoCentro = new CursoCentroCapacitacion();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (cursoCentro.getFechaInicio().after(cursoCentro.getFechaFin())) {
                FacesUtils.addErrorMessage("formPrincipal:txtFechaFin", "La Fecha Fin no puede ser menor a la Fecha Inicio.");
                return;
            }
            if (cursoCentro.getFechaInicioInscripcion().after(cursoCentro.getFechaFinInscripcion())) {
                FacesUtils.addErrorMessage("formPrincipal:txtFechaFinInscripcion", "La Fecha Fin Inscripción no puede ser menor a la Fecha Inicio Inscripción.");
                return;
            }

            if (cursoCentro.getId() == null) {
                /*if (!menuBarBean.getUsuario().verificarRolAnalista()) {
                    cursoCentro.setCentroCapacitacion(menuBarBean.getUsuario().getCentroCapacitacionAdministracionActual().getCentroCapacitacion());
                }*/
                cursoCentro.setCreadoPor(menuBarBean.getUsuario());
                cursoCentroFacadeLocal.create(cursoCentro);
            } else {
                cursoCentroFacadeLocal.edit(cursoCentro);
            }
            initCursoCentro();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
    }

    public void eliminar(CursoCentroCapacitacion cursoCentro) {
        try {
            cursoCentroFacadeLocal.remove(cursoCentro);
            initCursoCentro();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public CursoCentroCapacitacion getCursoCentro() {
        return cursoCentro;
    }

    public void setCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
    }

    public List<CursoCentroCapacitacion> getCursoCentroList() {
        if (menuBarBean.getUsuario().verificarRolAnalista()) {
            return cursoCentroList;
        } else {
            return cursoCentroList.stream()
                    .filter(cc -> cc.getCentroCapacitacion().equals(menuBarBean.getUsuario().getCentroCapacitacionAdministracionActual().getCentroCapacitacion()))
                    .collect(Collectors.toList());
        }
    }

    /**
     *
     * @return Listado de cursos continuos por centro capacitacion activos para
     * inscripcion
     */
    public List<CursoCentroCapacitacion> getCursoCentroActivoList() {
        if (cursoCentroList == null) {
            return new ArrayList<>();
        }
        Date hoy = new Date();
        return cursoCentroList.stream()
                .filter(cc -> cc.isCursoContinuo() && cc.isActivoParaInscripcion())
                .collect(Collectors.toList());
    }

    public void setCursoCentroList(List<CursoCentroCapacitacion> cursoCentroList) {
        this.cursoCentroList = cursoCentroList;
    }

    public void seleccionarCurso() {
        //cursoCentro.setProyecto(cursoCentro.getCurso().getProyecto());
        cursoCentro.setModalidad(cursoCentro.getCurso().getModalidad());
        cursoCentro.setTipoCertificado(cursoCentro.getCurso().getTipoCertificado());
        cursoCentro.setPrecio(cursoCentro.getCurso().getPrecio());
        cursoCentro.setNroHoras(cursoCentro.getCurso().getNroHoras());
    }

    public List<String> getCentroCapacitacionFiltroList() {
        return getCursoCentroActivoList().stream()
                .map(c -> c.getCentroCapacitacion().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getProgramaFiltroList() {
        return getCursoCentroActivoList().stream()
                .map(c -> c.getCurso().getPrograma().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCursoFiltroList() {
        return getCursoCentroActivoList().stream()
                .map(c -> c.getCurso().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getModalidadFiltroList() {
        return getCursoCentroActivoList().stream()
                .map(c -> c.getModalidadDescripcion())
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean validarHora(FacesContext context, List<UIInput> components, List<Object> values) {
        if (values.get(0) == null && values.get(1) == null) {
            return true;
        }
        if (values.get(0) == null || values.get(1) == null) {
            return false;
        }
        Date hora1 = (Date) values.get(0);
        Date hora2 = (Date) values.get(1);
        return hora2.after(hora1);
    }

}
