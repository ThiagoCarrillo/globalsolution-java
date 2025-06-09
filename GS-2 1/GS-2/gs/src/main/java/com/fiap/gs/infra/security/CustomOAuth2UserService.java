package com.fiap.gs.infra.security;

import com.fiap.gs.model.usuario.Usuario;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Cria usuário em memória
        Usuario usuario = criarUsuario(attributes);

        return new CustomOAuth2User(usuario, attributes);
    }

    private Usuario criarUsuario(Map<String, Object> attributes) {
        Usuario usuario = new Usuario();
        usuario.setNome((String) attributes.get("name"));
        usuario.setEmail((String) attributes.get("email"));
        return usuario;
    }
}