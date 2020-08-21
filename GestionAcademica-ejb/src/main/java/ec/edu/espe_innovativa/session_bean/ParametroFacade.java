package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Parametro;
import javax.ejb.Stateless;

@Stateless
public class ParametroFacade extends AbstractFacade<Parametro> implements ParametroFacadeLocal {

    public ParametroFacade() {
        super(Parametro.class);
    }
    
}
