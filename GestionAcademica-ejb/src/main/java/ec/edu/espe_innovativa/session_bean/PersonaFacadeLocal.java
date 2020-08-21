package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Persona;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PersonaFacadeLocal {

    void create(Persona persona);

    void edit(Persona persona);

    void remove(Persona persona);

    Persona find(Object id);
    
    List<Persona> findAll();

    List<Persona> findRange(int[] range);

    int count();
    
}
