package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Rol;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RolFacade extends AbstractFacade<Rol> implements RolFacadeLocal {

     public RolFacade() {
        super(Rol.class);
    }
    
}
