package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Programa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface ProgramaFacadeLocal {

    void create(Programa programa);

    void edit(Programa programa);

    void remove(Programa programa);

    Programa find(Object id);

    List<Programa> findAll();

    List<Programa> findRange(int[] range);

    int count();
    
}
