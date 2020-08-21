/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class MensajeCorreoFacade extends AbstractFacade<MensajeCorreo> implements MensajeCorreoFacadeLocal {
    public MensajeCorreoFacade() {
        super(MensajeCorreo.class);
    }
    
}
