package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class CursoCentroCapacitacionFacade extends AbstractFacade<CursoCentroCapacitacion> implements CursoCentroCapacitacionFacadeLocal {

    public CursoCentroCapacitacionFacade() {
        super(CursoCentroCapacitacion.class);
    }

    @Override
    public void create(CursoCentroCapacitacion cursoCentroCapacitacion, List<CentroCapacitacion> centroList) {
        for (CentroCapacitacion centro : centroList) {
            cursoCentroCapacitacion.setId(null);
            cursoCentroCapacitacion.setCentroCapacitacion(centro);
            create(cursoCentroCapacitacion);
        }
    }
    
}
