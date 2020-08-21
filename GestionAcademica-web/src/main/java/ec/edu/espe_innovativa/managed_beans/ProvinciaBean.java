package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Parroquia;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.session_bean.CantonFacadeLocal;
import ec.edu.espe_innovativa.session_bean.ParroquiaFacadeLocal;
import ec.edu.espe_innovativa.session_bean.ProvinciaFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.TabChangeEvent;

@Named(value = "provinciaBean")
@ViewScoped
public class ProvinciaBean implements Serializable {

    @EJB
    private ProvinciaFacadeLocal provinciaFacadeLocal;
    private List<Provincia> provinciaList;
    private Provincia provincia;
    @EJB
    private CantonFacadeLocal cantonFacadeLocal;
    private List<Canton> cantonList;
    private Canton canton;
    @EJB
    private ParroquiaFacadeLocal parroquiaFacadeLocal;
    private List<Parroquia> parroquiaList;
    private Parroquia parroquia;

    public ProvinciaBean() {
    }

    @PostConstruct
    private void init() {
        initProvincias();
        initCantones();
        initParroquias();
    }

    private void initProvincias() {
        provincia = null;
        provinciaList = provinciaFacadeLocal.findAll();
        //provinciaList = provinciaFacadeLocal.findByName("Manabi");
    }

    private void initCantones() {
        canton = null;
        cantonList = cantonFacadeLocal.findAll();
    }

    private void initParroquias() {
        parroquia = null;
        parroquiaList = parroquiaFacadeLocal.findAll();
    }

    public void nuevo() {
        provincia = new Provincia();
    }

    public void cancelar() {
        initProvincias();
    }

    public void grabar() {
        try {
            if (provincia.getId() == null) {
                provinciaFacadeLocal.create(provincia);
            } else {
                provinciaFacadeLocal.edit(provincia);
            }
            init();
            FacesUtils.addMessageRegistroGrabado();

        } catch (Exception e) {
            try {
                if (e.getCause().getCause().getMessage().contains("(codigo)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtCodigo", "Ya existe una Provincia con este código.");
                    return;
                }
                if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtNombre", "Ya existe una Provincia con este nombre.");
                    return;
                }
            } catch (Exception e1) {
            }

            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(Provincia provincia) {
        this.provincia = provincia;
    }

    public void eliminar(Provincia provincia) {
        try {
            provinciaFacadeLocal.remove(provincia);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public void nuevoCanton() {
        canton = new Canton();
    }

    public void cancelarCanton() {
        initCantones();
    }

    public void grabarCanton() {
        try {
            if (canton.getId() == null) {
                cantonFacadeLocal.create(canton);
            } else {
                cantonFacadeLocal.edit(canton);
            }
            initCantones();
            initParroquias();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            try {
                if (e.getCause().getCause().getMessage().contains("(codigo, id_provincia)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtCodigoCan", "Ya existe un Cantón con este código dentro de la Provincia seleccionada.");
                    return;
                }
                if (e.getCause().getCause().getMessage().contains("(id_provincia, nombre)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtNombreCan", "Ya existe un Cantón con este nombre dentro de la Provincia seleccionada.");
                    return;
                }
            } catch (Exception e1) {
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionarCanton(Canton canton) {
        this.canton = canton;
    }

    public void eliminarCanton(Canton canton) {
        try {
            cantonFacadeLocal.remove(canton);
            initCantones();
            initParroquias();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public void nuevoParoquia() {
        parroquia = new Parroquia();
    }

    public void cancelarParroquia() {
        initParroquias();
    }

    public void grabarParroquia() {
        try {
            if (parroquia.getId() == null) {
                parroquiaFacadeLocal.create(parroquia);
            } else {
                parroquiaFacadeLocal.edit(parroquia);
            }
            initParroquias();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            try {
                if (e.getCause().getCause().getMessage().contains("(id_canton, codigo)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtCodigoParr", "Ya existe una Parroquia con este código dentro del Cantón Seleccionado.");
                    return;
                }
                if (e.getCause().getCause().getMessage().contains("(id_canton, nombre)")) {
                    FacesUtils.addErrorMessage("formPrincipal:tab:txtNombreParr", "Ya existe una Parroquia con este nombre dentro del Cantón Seleccionado.");
                    return;
                }
            } catch (Exception e1) {
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionarParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
        provincia = parroquia.getCanton().getProvincia();
    }

    public void eliminarParroquia(Parroquia parroquia) {
        try {
            parroquiaFacadeLocal.remove(parroquia);
            initParroquias();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public void onTabChange(TabChangeEvent event) {
        provincia = null;
        canton = null;
        parroquia = null;
    }

    public List<Provincia> getProvinciaList() {
        return provinciaList;
    }
    public List<Provincia> getProvinciaActivoList() {
        if (provinciaList == null)
            return new ArrayList<>();
        return provinciaList.stream().filter(p -> p.getEstado()=='A').collect(Collectors.toList());
    }

    public void setProvinciaList(List<Provincia> provinciaList) {
        this.provinciaList = provinciaList;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Canton> getCantonList() {
        return cantonList;
    }

    public void setCantonList(List<Canton> cantonList) {
        this.cantonList = cantonList;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public List<Parroquia> getParroquiaList() {
        return parroquiaList;
    }

    public void setParroquiaList(List<Parroquia> parroquiaList) {
        this.parroquiaList = parroquiaList;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

}
