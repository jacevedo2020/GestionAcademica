/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Parroquia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface ParroquiaFacadeLocal {

    void create(Parroquia parroquia);

    void edit(Parroquia parroquia);

    void remove(Parroquia parroquia);

    Parroquia find(Object id);

    List<Parroquia> findAll();

    List<Parroquia> findRange(int[] range);

    int count();
    
}
