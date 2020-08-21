package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Usuario;
import java.util.List;
import javax.ejb.Local;

@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    Usuario findByIdentificacion(String identificacion);
    
    Usuario findByIdentificacionEmail(String identificacion, String email);
    
    List<Usuario> findByRol(Integer idRol);
}
