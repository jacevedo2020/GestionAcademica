package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import javax.ejb.Stateless;

@Stateless
public class TipoIdentificacionFacade extends AbstractFacade<TipoIdentificacion> implements TipoIdentificacionFacadeLocal {

    public TipoIdentificacionFacade() {
        super(TipoIdentificacion.class);
    }
    
}
