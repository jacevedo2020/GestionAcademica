package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.RolOpcion;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RolOpcionFacadeLocal {

    void create(RolOpcion rolOpcion);

    void edit(RolOpcion rolOpcion);

    void remove(RolOpcion rolOpcion);

    RolOpcion find(Object id);

    List<RolOpcion> findAll();

    List<RolOpcion> findRange(int[] range);

    int count();
    
}
