package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Parametro;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ParametroFacadeLocal {

    void create(Parametro parametro);

    void edit(Parametro parametro);

    void remove(Parametro parametro);

    Parametro find(Object id);

    List<Parametro> findAll();

    List<Parametro> findRange(int[] range);

    int count();
    
}
