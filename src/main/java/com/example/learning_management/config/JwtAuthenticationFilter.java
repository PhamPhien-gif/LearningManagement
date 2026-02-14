package com.example.learning_management.config;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.learning_management.token.TokenRepository;
import com.example.learning_management.token.TokenType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    )throws ServletException, IOException {
        
        //auth api: skip to next filter
        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        
        //does not have bearer token: skip to next filter
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
      
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        //check if find userEmail and does not have userDetails in SecurityContextHolder
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            
            //check expired and revoked in database
            var token = tokenRepository.findByToken(jwt).orElse(null);
            final boolean isValidToken;
            if(token == null || token.isExpired() || token.isRevoked() && token.getTokenType() == TokenType.ACCESS_TOKEN){
                isValidToken = false;
            }else{
                isValidToken = true;
            }

            if(jwtService.isTokenvalid(jwt, userDetails) && isValidToken){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
                
                //don't mind this yet
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

                //store in SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            

        }
        filterChain.doFilter(request, response);

    }

}
