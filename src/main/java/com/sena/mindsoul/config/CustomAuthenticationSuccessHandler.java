package com.sena.mindsoul.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

//Clase para customizar las rutas exitosas

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // Ruta aAdministrador
    SimpleUrlAuthenticationSuccessHandler adminSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/admin");

    // Ruta Psicologo
    SimpleUrlAuthenticationSuccessHandler psicologoSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/psicologo");

    // Ruta super administrador
    SimpleUrlAuthenticationSuccessHandler userSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/paciente");

    // Metodo que lo verifica
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();

            if (authorityName.equals("ROLE_PSICOLOGO")) {
                // if the user NOT is an ADMIN delegate to the USERSuccessHandler
                this.psicologoSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
            if (authorityName.equals("ROLE_PACIENTE")) {
                // if the user NOT is an ADMIN delegate to the USERSuccessHandler
                this.psicologoSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }

            if (authorityName.equals("ROLE_USER")) {
                // if the user NOT is an ADMIN delegate to the USERSuccessHandler
                this.userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }

        }
        // if the user is an admin delegate to the ADMINSuccessHandler
        this.adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
