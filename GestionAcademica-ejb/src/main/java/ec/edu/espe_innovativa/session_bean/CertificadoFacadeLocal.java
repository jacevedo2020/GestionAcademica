package ec.edu.espe_innovativa.session_bean;

import ec.edu.espe_innovativa.entity_bean.Certificado;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CertificadoFacadeLocal {

    void create(Certificado certificado);

    void edit(Certificado certificado);

    void remove(Certificado certificado);

    Certificado find(Object id);

    List<Certificado> findAll();

    List<Certificado> findRange(int[] range);

    int count();
    
    Integer getMaximoId();
    
}
