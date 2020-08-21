package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CentroCapacitacionFacade extends AbstractFacade<CentroCapacitacion> implements CentroCapacitacionFacadeLocal {

    public CentroCapacitacionFacade() {
        super(CentroCapacitacion.class);
    }
    
}
