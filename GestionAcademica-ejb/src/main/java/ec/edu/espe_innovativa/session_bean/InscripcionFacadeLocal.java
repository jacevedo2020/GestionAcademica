package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface InscripcionFacadeLocal {

    void create(Inscripcion inscripcion);

    void createOrEditInscripcionEmpresa(Inscripcion inscripcion);
    
    void edit(Inscripcion inscripcion);

    void remove(Inscripcion inscripcion);
    
    Inscripcion find(Object id);

    List<Inscripcion> findAll();

    List<Inscripcion> findByIniciadas();

    List<Inscripcion> findByCursoCorporativo();
    
    List<Inscripcion> findRange(int[] range);

    int count();
    
}
