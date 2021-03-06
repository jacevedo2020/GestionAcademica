/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Canton;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface CantonFacadeLocal {

    void create(Canton canton);

    void edit(Canton canton);

    void remove(Canton canton);

    Canton find(Object id);

    List<Canton> findAll();

    List<Canton> findRange(int[] range);

    int count();
    
}
