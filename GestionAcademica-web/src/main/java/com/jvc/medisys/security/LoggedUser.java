package com.jvc.medisys.security;

import ec.edu.espe_innovativa.entity_bean.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoggedUser implements UserDetails {

    private Usuario user;
    private List<String> roles;

    public LoggedUser(Usuario user, List<String> roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getClave();
    }

    @Override
    public String getUsername() {
        return user.getPersona().getIdentificacion();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return (user.getRolesList()!=null && !user.getRolesList().isEmpty());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.getClaveExpirada().equals('S');
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEstado().equals('A');
    }

}
