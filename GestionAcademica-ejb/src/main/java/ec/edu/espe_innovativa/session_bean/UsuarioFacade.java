package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public Usuario findByIdentificacion(String identificacion) {
        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.persona.identificacion = ?1");
            q.setParameter(1, identificacion);
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Usuario findByIdentificacionEmail(String identificacion, String email) {
        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.persona.identificacion = ?1 and u.persona.email = ?2");
            q.setParameter(1, identificacion);
            q.setParameter(2, email);
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Usuario> findByRol(Integer idRol) {
        Query q = em.createQuery("SELECT u.usuario FROM UsuarioRol u WHERE u.rol.id = ?1 and u.usuario.estado='A'");
        q.setParameter(1, idRol);
        return q.getResultList();
    }

}
