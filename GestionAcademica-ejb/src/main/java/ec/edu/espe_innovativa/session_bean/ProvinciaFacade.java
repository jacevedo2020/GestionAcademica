/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author admin
 */
@Stateless
public class ProvinciaFacade extends AbstractFacade<Provincia> implements ProvinciaFacadeLocal {

    public ProvinciaFacade() {
        super(Provincia.class);
    }

    private String removeAccents(String cadena) {
        return cadena.replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u');
    }

    /*@Override
    public List<Provincia> findByName(String nombre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provincia> cq = cb.createQuery(Provincia.class);
        Root<Provincia> root = cq.from(Provincia.class);

        cq.from(Provincia.class);
        From from = root;

        Predicate predicate = cb.like(cb.function("eliminarTildes", String.class, cb.lower(from.get("nombre"))), "%" + removeAccents(nombre.toLowerCase()) + "%");
        cq.where(predicate);
        return em.createQuery(cq).getResultList();
    }*/
    @Override
    public List<Provincia> findByName(String nombre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provincia> cq = cb.createQuery(Provincia.class);
        Root<Provincia> provincias = cq.from(Provincia.class);
        Join<Provincia, Canton> cantones = provincias.join("cantonList");

        cq.select(provincias);
        Predicate predicate1 = cb.like(cb.function("eliminarTildes", String.class, cb.lower(provincias.get("nombre"))), "%" + removeAccents(nombre.toLowerCase()) + "%");
        Predicate predicate2 = cb.like(cb.function("eliminarTildes", String.class, cb.lower(cantones.get("nombre"))), "%" + removeAccents(nombre.toLowerCase()) + "%");
        Predicate predicate3 = cb.or(predicate2);
        cq.select(provincias).where(predicate3);
        return em.createQuery(cq).getResultList();
    }

}
