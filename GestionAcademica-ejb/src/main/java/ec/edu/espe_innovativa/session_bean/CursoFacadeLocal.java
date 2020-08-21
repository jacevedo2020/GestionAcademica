package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Curso;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CursoFacadeLocal {

    void create(Curso curso);

    void create(List<Curso> cursos);

    void edit(Curso curso);

    void remove(Curso curso);

    Curso find(Object id);

    List<Curso> findAll();

    List<Curso> findRange(int[] range);

    int count();
    
}
