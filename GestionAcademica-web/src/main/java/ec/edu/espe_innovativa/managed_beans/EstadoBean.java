package ec.edu.espe_innovativa.managed_beans;

import ec.edu.espe_innovativa.entity_bean.Estado;
import ec.edu.espe_innovativa.session_bean.EstadoFacadeLocal;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Named(value = "estadoBean")
@ApplicationScoped
public class EstadoBean implements Serializable{

    @EJB
    private EstadoFacadeLocal estadoFacadeLocal;
    private List<Estado> estadoList;
    
    
    public EstadoBean() {
    }
    
    @PostConstruct
    public void init(){
        estadoList = estadoFacadeLocal.findAll();
    }
 
    public List<String> getEstadoDescripcionList() {
        return estadoList.stream()
                .sorted((e1,e2)->e1.getId().compareTo(e2.getId()))
                .map(e -> e.getDescripcion())
                .distinct()
                .collect(Collectors.toList());
    }

}
