package ec.edu.espe_innovativa.managed_beans;

import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.session_bean.CantonFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "cantonBean")
@ViewScoped
public class CantonBean implements Serializable{

    @EJB
    private CantonFacadeLocal cantonFacadeLocal;
    private List<Canton> cantonList;
    public CantonBean() {
    }
    
    @PostConstruct
    public void init(){
        cantonList = cantonFacadeLocal.findAll();
        cantonList.sort((c1,c2)->c1.getCabeceraCantonal().compareTo(c2.getCabeceraCantonal()));
    }

    public List<Canton> getCantonList() {
        return cantonList;
    }

    public void setCantonList(List<Canton> cantonList) {
        this.cantonList = cantonList;
    }
    
    
}
