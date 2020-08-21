package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Persona;
import javax.ejb.Stateless;

@Stateless
public class PersonaFacade extends AbstractFacade<Persona> implements PersonaFacadeLocal {

    public PersonaFacade() {
        super(Persona.class);
    }
    
}
