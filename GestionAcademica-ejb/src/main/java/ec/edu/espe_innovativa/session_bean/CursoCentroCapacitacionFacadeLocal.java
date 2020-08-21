package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CursoCentroCapacitacionFacadeLocal {

    void create(CursoCentroCapacitacion cursoCentroCapacitacion);

    void create(CursoCentroCapacitacion cursoCentroCapacitacion, List<CentroCapacitacion> centroList);

    void edit(CursoCentroCapacitacion cursoCentroCapacitacion);

    void remove(CursoCentroCapacitacion cursoCentroCapacitacion);

    CursoCentroCapacitacion find(Object id);

    List<CursoCentroCapacitacion> findAll();

    List<CursoCentroCapacitacion> findRange(int[] range);

    int count();
    
}
