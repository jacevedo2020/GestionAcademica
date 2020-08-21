package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CentroCapacitacionFacadeLocal {

    void create(CentroCapacitacion centroCapacitacion);

    void edit(CentroCapacitacion centroCapacitacion);

    void remove(CentroCapacitacion centroCapacitacion);

    CentroCapacitacion find(Object id);

    List<CentroCapacitacion> findAll();

    List<CentroCapacitacion> findRange(int[] range);

    int count();
    
}
