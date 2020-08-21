package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Rol;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RolFacadeLocal {

    void create(Rol rol);

    void edit(Rol rol);

    void remove(Rol rol);

    Rol find(Object id);

    List<Rol> findAll();

    List<Rol> findRange(int[] range);

    int count();
    
}
