/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.TipoEstudiante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author admin
 */
@Local
public interface TipoEstudianteFacadeLocal {

    void create(TipoEstudiante tipoEstudiante);

    void edit(TipoEstudiante tipoEstudiante);

    void remove(TipoEstudiante tipoEstudiante);

    TipoEstudiante find(Object id);

    List<TipoEstudiante> findAll();

    List<TipoEstudiante> findRange(int[] range);

    int count();
    
}
