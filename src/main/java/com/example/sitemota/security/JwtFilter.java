package com.example.sitemota.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.sitemota.dto.UserFilter;
import com.example.sitemota.model.User;
import com.example.sitemota.repository.UserRepository;
import com.example.sitemota.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository repository;


    public JwtFilter(JwtService jwtService, UserRepository repository) {
        this.jwtService = jwtService;
        this.repository = repository;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {

            String email = jwtService.extrairEmail(token);
            String role = jwtService.extrairRole(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                User usuario = repository.findByEmail(email).orElseThrow();
                UserFilter responseUser = new UserFilter(usuario.getId(),usuario.getNome() ,usuario.getEmail(), usuario.getRole());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                responseUser,
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}