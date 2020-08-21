package com.jvc.medisys.security;

import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppService appService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario usuario = null;
        
        try {
            usuario = appService.getUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("aaa");
        }
        if (usuario == null) {
            throw new UsernameNotFoundException("User doesn`t exist");
        }
        //Fetching User roles form DB.

        System.out.println("Password: " + usuario.getClave());
        List<String> dbRoles = new ArrayList<String>();
        for (Rol userRole : usuario.getRolesList()) {
            dbRoles.add(userRole.getNombre());
        }
        System.out.println("Roles of :" + username + " is " + dbRoles);

        LoggedUser loggedUser = new LoggedUser(usuario, dbRoles);
        return loggedUser;
    }

}
