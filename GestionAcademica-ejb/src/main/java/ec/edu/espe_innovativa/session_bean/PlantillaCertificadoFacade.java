package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import java.util.Date;
import javax.ejb.Stateless;

@Stateless
public class PlantillaCertificadoFacade extends AbstractFacade<PlantillaCertificado> implements PlantillaCertificadoFacadeLocal {

    public PlantillaCertificadoFacade() {
        super(PlantillaCertificado.class);
    }

    @Override
    public void create(PlantillaCertificado entity) {
        entity.setFechaCreacion(new Date());
        super.create(entity); 
    }
    
    
    
}
