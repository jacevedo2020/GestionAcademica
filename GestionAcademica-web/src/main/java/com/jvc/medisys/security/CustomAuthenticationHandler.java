package com.jvc.medisys.security;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.RolOpcion;
import ec.edu.espe_innovativa.session_bean.RolOpcionFacadeLocal;
import java.io.IOException;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UrlCache urlCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        try {

            /*Context c = new InitialContext();
            OpcionPerfilServicio us = (OpcionPerfilServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/OpcionPerfilServicio!com.jvc.medisys.servicios.OpcionPerfilServicio");
            List<OpcionPerfil> roleActions = us.listar();
            for (OpcionPerfil urlRolesBean : roleActions) {
                System.out.println("Role Name " + urlRolesBean.getPerCodigo().getPerDescrip());
                System.out.println("Action Name " + urlRolesBean.getOpcCodigo().getOpcPath());
            }
            urlCache.mapUrlToRole(roleActions);*/
            Context c = new InitialContext();
            RolOpcionFacadeLocal us = (RolOpcionFacadeLocal) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/RolOpcionFacade!ec.edu.espe_innovativa.session_bean.RolOpcionFacadeLocal");
            List<RolOpcion> roleActions = us.findAll();
            for (RolOpcion urlRolesBean : roleActions) {
                System.out.println("Role Name " + urlRolesBean.getRol().getNombre());
                System.out.println("Action Name " + urlRolesBean.getOpcion().getUrl());
            }
            urlCache.mapUrlToRole(roleActions);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String userTargetUrl = "/home.xhtml";
        getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
    }
}
