package com.jvc.medisys.security;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Opcion;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.RolOpcion;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.RolFacadeLocal;
import ec.edu.espe_innovativa.session_bean.RolOpcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppService {

    @Autowired
    private UrlCache urlCache;

    @Transactional(readOnly = true)
    public Usuario getUser(String username) throws NamingException {
        Context c = new InitialContext();
        /*UsuarioServicio us = (UsuarioServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/UsuarioServicio!com.jvc.medisys.servicios.UsuarioServicio");
        return us.buscarUsuario(username);*/
        UsuarioFacadeLocal us = (UsuarioFacadeLocal) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/UsuarioFacade!ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal");
        return us.findByIdentificacion(username);
    }

    @Transactional(readOnly = true)
    public void getUrlRoles() throws NamingException {
        /*Context c = new InitialContext();
        OpcionPerfilServicio us = (OpcionPerfilServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/OpcionPerfilServicio!com.jvc.medisys.servicios.OpcionPerfilServicio");
        List<OpcionPerfil> roleActions = us.listar();
        PerfilServicio os = (PerfilServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/PerfilServicio!com.jvc.medisys.servicios.PerfilServicio");
        List<Perfil> perfiles = os.listarPerfiles();
        for (Perfil p : perfiles) {
            OpcionPerfil op = new OpcionPerfil();
            
            Opcion opcion = new Opcion();
            opcion.setOpcPath("/faces/plantilla/main-template.xhtml");

            op.setPerCodigo(p);
            op.setOpcCodigo(opcion);
            roleActions.add(op);
        }

        urlCache.mapUrlToRole(roleActions);
        return;*/

        Context c = new InitialContext();
        RolOpcionFacadeLocal us = (RolOpcionFacadeLocal) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/RolOpcionFacade!ec.edu.espe_innovativa.session_bean.RolOpcionFacadeLocal");
        List<RolOpcion> roleActions = us.findAll();
        RolFacadeLocal os = (RolFacadeLocal) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/RolFacade!ec.edu.espe_innovativa.session_bean.RolFacadeLocal");
        List<Rol> perfiles = os.findAll();
        for (Rol p : perfiles) {
            RolOpcion op = new RolOpcion();
            Opcion opcion = new Opcion();
            opcion.setUrl("/plantilla/plantilla.xhtml");
            op.setRol(p);
            op.setOpcion(opcion);
            roleActions.add(op);
            
            op = new RolOpcion();
            opcion = new Opcion();
            opcion.setUrl("/home.xhtml");
            op.setRol(p);
            op.setOpcion(opcion);
            roleActions.add(op);
        }

        urlCache.mapUrlToRole(roleActions);
        return;
    }
}
