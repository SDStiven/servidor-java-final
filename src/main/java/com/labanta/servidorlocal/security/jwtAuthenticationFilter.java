package com.labanta.servidorlocal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class jwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public jwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // token: "Bearer usciushusduugdoscg"
        // token: "Bearer "
        // token: null

        if (authHeader == null || !authHeader.startsWith("Bearer") || authHeader.split(" ")[1] == ""
                || authHeader.split(" ")[1] == "undefined") {
            filterChain.doFilter(request, response);
            return;
        }
        // extrair token ignorando os primeiros 7 caracter "Bearer"
        String token = authHeader.substring(7);

        // ignorar token vazio ou "undefine"(ex: frontend mal comfigurado)
        if (token.isEmpty() || token.equals("undefined")) {

            filterChain.doFilter(request, response);

            return;
        }

        try {
            // Extreir o user name do token (ista tambem valida a assinatura e a expiração)
            String username = jwtService.extrairUsername(token);

            // se o username é valido e ainda não ha autenticação no contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Dizer a spring que este utilizador esta autenticado
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
                        new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        } catch (Exception e) {
            // Tooken invalido ou expirou- nao autenticar ,o spring vai devolver 401
        }
        filterChain.doFilter(request, response);

    }
}
