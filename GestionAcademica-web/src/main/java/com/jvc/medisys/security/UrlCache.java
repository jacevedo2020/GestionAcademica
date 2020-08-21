package com.jvc.medisys.security;

import ec.edu.espe_innovativa.entity_bean.RolOpcion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UrlCache {

    HashMap<String, List<String>> urlRoles = new HashMap<String, List<String>>();

    public HashMap<String, List<String>> getUrlRoles() {
        return this.urlRoles;
    }

    public void setUrlRoles(HashMap<String, List<String>> urlRoles) {
        this.urlRoles = urlRoles;
    }

    public List<String> getUrlRoles(String key) {
        return urlRoles.get(key);
    }

    /**
     *
     * @param roleActions Maps the Url as key and List<String> as the value.
     */
    public void mapUrlToRole(List<RolOpcion> roleActions) {
        /*String dbUrl = null;
        for (OpcionPerfil urlRolesBean : roleActions) {
            dbUrl = urlRolesBean.getOpcCodigo().getOpcPath();
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                int pos = dbUrl.indexOf("?");
                if (pos != -1) {
                    dbUrl = dbUrl.substring(0, pos);
                }
                if (this.urlRoles.containsKey(dbUrl)) {
                    List<String> roles = this.urlRoles.get(dbUrl);
                    roles.add(urlRolesBean.getPerCodigo().getPerDescrip());

                } else {
                    List<String> roles = new ArrayList<String>();
                    roles.add(urlRolesBean.getPerCodigo().getPerDescrip());
                    this.urlRoles.put(dbUrl, roles);
                }
            }
        }*/

        String dbUrl = null;
        for (RolOpcion urlRolesBean : roleActions) {
            dbUrl = urlRolesBean.getOpcion().getUrl();
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                int pos = dbUrl.indexOf("?");
                if (pos != -1) {
                    dbUrl = dbUrl.substring(0, pos);
                }
                if (this.urlRoles.containsKey(dbUrl)) {
                    List<String> roles = this.urlRoles.get(dbUrl);
                    roles.add(urlRolesBean.getRol().getNombre());

                } else {
                    List<String> roles = new ArrayList<String>();
                    roles.add(urlRolesBean.getRol().getNombre());
                    this.urlRoles.put(dbUrl, roles);
                }
            }
        }
    }

}
