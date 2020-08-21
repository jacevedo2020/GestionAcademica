package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Programa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class ProgramaFacade extends AbstractFacade<Programa> implements ProgramaFacadeLocal {

    public ProgramaFacade() {
        super(Programa.class);
    }
    
}
