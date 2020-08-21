package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Certificado;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CertificadoFacade extends AbstractFacade<Certificado> implements CertificadoFacadeLocal {

    public CertificadoFacade() {
        super(Certificado.class);
    }

    @Override
    public Integer getMaximoId() {
        Query q = em.createQuery("SELECT COALESCE(MAX(o.id),0) FROM Certificado o");
        return (Integer) q.getSingleResult();
    }
    
}
