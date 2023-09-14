package com.capg.User.JwtConfig;

import com.capg.User.Service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header=request.getHeader("Authorization");
        String token=null;

        if(header!=null && header.startsWith("Bearer")) {

            token=header.substring(7);

            if(jwtUtils.validateToken(token)) {

                String username=jwtUtils.getUsername(token);

                UserDetails userdetails= userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(username, null, userdetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            }

        }
        filterChain.doFilter(request, response);
    }
}
