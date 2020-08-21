package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Proyecto;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ProyectoFacadeLocal {

    void create(Proyecto proyecto);

    void edit(Proyecto proyecto);

    void remove(Proyecto proyecto);

    Proyecto find(Object id);

    List<Proyecto> findAll();

    List<Proyecto> findRange(int[] range);

    int count();
    
}
