package com.jvc.medisys.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Srinivas Nalla
 *
 *
 */
@Component
public class DbFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    @Autowired
    private AppService appService;

    @Autowired
    private UrlCache urlCache;

    private HashMap<String, List<String>> urlRoles;

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        System.out.println("Request Url====>" + url);
        int pos = url.indexOf("?");
        if (pos != -1) {
            url = url.substring(0, pos);
        }

        List<String> roles_ = urlRoles.get(url);
        System.out.println("Url Associated Roles :" + roles_);
        if (roles_ == null) {
            /*if (url.equals("/login/faces/plantilla/main-template.xhtml")) {
                roles_= new ArrayList<>();
                roles_.add("ADMINISTRADOR DE SEGURIDADES");
            } else {*/
            return null;
            //}
        }
        System.out.println("------------------");
        String[] stockArr = new String[roles_.size()];
        stockArr = roles_.toArray(stockArr);

        return SecurityConfig.createList(stockArr);
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public void afterPropertiesSet() throws Exception {

        appService.getUrlRoles();
        this.urlRoles = urlCache.getUrlRoles();
        System.out.println("Url Roles object :" + urlRoles);
    }

}
