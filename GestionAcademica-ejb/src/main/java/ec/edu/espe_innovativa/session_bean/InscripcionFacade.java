package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Estado;
import ec.edu.espe_innovativa.entity_bean.EstadoInscripcion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class InscripcionFacade extends AbstractFacade<Inscripcion> implements InscripcionFacadeLocal {

    public InscripcionFacade() {
        super(Inscripcion.class);
    }

    @Override
    public List<Inscripcion> findByCursoCorporativo() {
        Query q = em.createQuery("SELECT o FROM Inscripcion o WHERE o.cursoCentroCapacitacion.curso.tipo = ?1 and o.tipo = ?2");
        q.setParameter(1, Curso.TIPO_CORPORATIVO);
        q.setParameter(2, Inscripcion.TIPO_EMPRESA);
        return q.getResultList();
    }

    @Override
    public void createOrEditInscripcionEmpresa(Inscripcion inscripcion) {
        Persona p = inscripcion.getEstudiante().getPersona();
        p.setTipoIdentificacion(inscripcion.getFacturaTipoIdentificacion());
        p.setIdentificacion(inscripcion.getFacturaRuc());
        p.setNombresCompletos(inscripcion.getFacturaRazonSocial());
        p.setDireccion(inscripcion.getFacturaDireccion());
        p.setEmail(inscripcion.getFacturaEmail());
        p.setTelefono(inscripcion.getFacturaTelefono());
        if (p.getId() == null) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        Usuario u = inscripcion.getEstudiante();
        if (u.getId() == null) {
            u.setClave("123");
            em.persist(u);
        }
        Curso curso = inscripcion.getCursoCentroCapacitacion().getCurso();
        if (curso.getId() == null) {
            em.persist(curso);
        } else {
            em.merge(curso);
        }

        CursoCentroCapacitacion cursoCentroCapacitacion = inscripcion.getCursoCentroCapacitacion();
        cursoCentroCapacitacion.setModalidad(curso.getModalidad());
        cursoCentroCapacitacion.setTipoCertificado(curso.getTipoCertificado());
        cursoCentroCapacitacion.setNroHoras(curso.getNroHoras());
        cursoCentroCapacitacion.setPrecio(curso.getPrecio());

        for (Inscripcion i : cursoCentroCapacitacion.getInscripcionList()) {
            if (i.getId() == null) {
                if (!i.isInscripcionEmpresa()) {
                    i.setInscripcionPadre(inscripcion);
                }
                EstadoInscripcion estadoInscripcion = new EstadoInscripcion();
                estadoInscripcion.setEstado(new Estado(Estado.ID_ESTADO_MATRICULA_REGISTRADA));
                estadoInscripcion.setUsuario(inscripcion.getCreadoPor());
                i.agregarEstado(estadoInscripcion);
            }
        }

        if (cursoCentroCapacitacion.getId() == null) {
            em.persist(cursoCentroCapacitacion);
        } else {
            em.merge(cursoCentroCapacitacion);
        }
        /*if (inscripcion.getId() == null) {
            em.persist(inscripcion);
        } else {
            em.merge(inscripcion);
        }*/
    }

    @Override
    public List<Inscripcion> findByIniciadas() {
        Query q = em.createQuery("SELECT o FROM Inscripcion o WHERE o.cursoCentroCapacitacion.fechaInicio <= CURRENT_DATE");
        return q.getResultList();
    }

}
