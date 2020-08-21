package ec.edu.espe_innovativa.managed_beans;

import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import ec.edu.espe_innovativa.session_bean.TipoIdentificacionFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "tipoIdentificacionBean")
@ViewScoped
public class TipoIdentificacionBean implements Serializable {

    @EJB
    private TipoIdentificacionFacadeLocal tipoIdentificacionFacadeLocal;
    private List<TipoIdentificacion> tipoIdentificacionList;

    public TipoIdentificacionBean() {
    }

    @PostConstruct
    private void init() {
        tipoIdentificacionList = tipoIdentificacionFacadeLocal.findAll();
    }

    public List<TipoIdentificacion> getTipoIdentificacionList() {
        return tipoIdentificacionList;
    }

    public List<TipoIdentificacion> getTipoIdentificacionParaFacturaList() {
        if (tipoIdentificacionList == null || tipoIdentificacionList.isEmpty()) {
            return new ArrayList<>();
        }
        return tipoIdentificacionList.stream().filter(t -> !Objects.equals(t.getId(), TipoIdentificacion.ID_TIPO_PASAPORTE)).collect(Collectors.toList());
    }
    public List<TipoIdentificacion> getTipoIdentificacionParaInscripcionList() {
        if (tipoIdentificacionList == null || tipoIdentificacionList.isEmpty()) {
            return new ArrayList<>();
        }
        return tipoIdentificacionList.stream().filter(t -> !Objects.equals(t.getId(), TipoIdentificacion.ID_TIPO_RUC)).collect(Collectors.toList());
    }

    public void setTipoIdentificacionList(List<TipoIdentificacion> tipoIdentificacionList) {
        this.tipoIdentificacionList = tipoIdentificacionList;
    }

}
