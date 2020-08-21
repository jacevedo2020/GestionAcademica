package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Parametro;
import ec.edu.espe_innovativa.session_bean.ParametroFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named(value = "parametroBean")
@ViewScoped
public class ParametroBean implements Serializable{

    @EJB
    private ParametroFacadeLocal parametroFacadeLocal;
    private List<Parametro> parametroList;
    private Parametro parametro;
    private String password;
    
    public ParametroBean() {
    }
    
    @PostConstruct
    public void init(){
        parametroList= parametroFacadeLocal.findAll();
        parametro = parametroList.get(0);
    }

    public void cancelar(){
        parametroList= parametroFacadeLocal.findAll();
        parametro=parametroList.stream().filter(p->p.equals(parametro)).findFirst().get();
    }
    public void grabar(){
        try {
            parametroFacadeLocal.edit(parametro);
            FacesUtils.addMessageRegistroGrabado();
            parametroList= parametroFacadeLocal.findAll();
            parametro=parametroList.stream().filter(p->p.equals(parametro)).findFirst().get();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
        
    }

    public List<Parametro> getParametroList() {
        return parametroList;
    }

    public void setParametroList(List<Parametro> parametroList) {
        this.parametroList = parametroList;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
