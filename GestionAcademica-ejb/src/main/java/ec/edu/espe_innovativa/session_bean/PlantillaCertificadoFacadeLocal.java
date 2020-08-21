package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PlantillaCertificadoFacadeLocal {

    void create(PlantillaCertificado plantillaCertificado);

    void edit(PlantillaCertificado plantillaCertificado);

    void remove(PlantillaCertificado plantillaCertificado);

    PlantillaCertificado find(Object id);

    List<PlantillaCertificado> findAll();

    List<PlantillaCertificado> findRange(int[] range);

    int count();
    
}
