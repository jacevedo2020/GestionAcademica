package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Curso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CursoFacade extends AbstractFacade<Curso> implements CursoFacadeLocal {

    public CursoFacade() {
        super(Curso.class);
    }

    @Override
    public void create(List<Curso> cursos) {
        for (Curso curso : cursos) {
            super.create(curso);
        }
    }
    
}
