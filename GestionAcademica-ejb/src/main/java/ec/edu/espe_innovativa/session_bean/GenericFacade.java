package ec.edu.espe_innovativa.session_bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericFacade {

    @PersistenceContext(unitName = "medisysPU")
    protected EntityManager em;
   
}
