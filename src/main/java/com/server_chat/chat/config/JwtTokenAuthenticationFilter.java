package com.server_chat.chat.config;

import com.server_chat.chat.entities.InstaUserDetails;
import com.server_chat.chat.services.JwtTokenProvider;
import com.server_chat.chat.services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private JwtTokenProvider tokenProvider;
    private UserService userService;

    public JwtTokenAuthenticationFilter(
            JwtConfig jwtConfig,
            JwtTokenProvider tokenProvider,
            UserService userService) {

        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


        String header = request.getHeader(jwtConfig.getHeader());

        if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfig.getPrefix(), "");

        if(tokenProvider.validateToken(token)) {
            Claims claims = tokenProvider.getClaimsFromJWT(token);
            String username = claims.getSubject();

            UsernamePasswordAuthenticationToken auth =
                    userService.findByUsername(username)
                            .map(InstaUserDetails::new)
                            .map(userDetails -> {
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());
                                authentication
                                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                return authentication;
                            })
                            .orElse(null);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
