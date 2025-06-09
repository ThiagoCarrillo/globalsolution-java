package com.fiap.gs.infra.security;

import com.fiap.gs.model.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Usuario usuario;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Usuario usuario, Map<String, Object> attributes) {
        this.usuario = usuario;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return usuario.getNome();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}