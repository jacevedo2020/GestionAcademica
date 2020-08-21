package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import java.util.List;
import javax.ejb.Local;

@Local
public interface TipoIdentificacionFacadeLocal {

    void create(TipoIdentificacion tipoIdentificacion);

    void edit(TipoIdentificacion tipoIdentificacion);

    void remove(TipoIdentificacion tipoIdentificacion);

    TipoIdentificacion find(Object id);

    List<TipoIdentificacion> findAll();

    List<TipoIdentificacion> findRange(int[] range);

    int count();
    
}
