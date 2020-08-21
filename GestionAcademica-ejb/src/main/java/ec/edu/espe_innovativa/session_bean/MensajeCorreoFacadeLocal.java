/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface MensajeCorreoFacadeLocal {

    void create(MensajeCorreo mensajeCorreo);

    void edit(MensajeCorreo mensajeCorreo);

    void remove(MensajeCorreo mensajeCorreo);

    MensajeCorreo find(Object id);

    List<MensajeCorreo> findAll();

    List<MensajeCorreo> findRange(int[] range);

    int count();
    
}
