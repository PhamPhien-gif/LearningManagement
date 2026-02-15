package com.example.learning_management.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import com.example.learning_management.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        //Check header
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        //Get userEmail
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        if(userEmail==null ){
            return;
        }

        //revoke user's tokens and clear context
        tokenService.revokeAllUserTokens(userEmail);
        SecurityContextHolder.clearContext();
    
    }
}
