package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Proyecto;
import javax.ejb.Stateless;

@Stateless
public class ProyectoFacade extends AbstractFacade<Proyecto> implements ProyectoFacadeLocal {

    public ProyectoFacade() {
        super(Proyecto.class);
    }
    
}
