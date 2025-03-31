package com.golmart.config.jwt;

import com.golmart.entity.UserDetailsImpl;
import com.golmart.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class. For any incoming request this Filter
class gets executed. It checks if the request has a valid JWT token. If it has a valid JWT Token then it sets the
 Authentication in the context, to specify that the current user is authenticated.
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * If the request has a valid JWT token, then set the authentication in the security context
     *
     * @param request  The request object.
     * @param response The response object.
     * @param chain    The FilterChain object represents the chain of filters that the request will pass through.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        if (uri.equals("/")
                || uri.contains("/images")
                || uri.contains("/catalog")
                || uri.contains("/css")
                || uri.contains("/login")
                || uri.contains("/static")
                || uri.contains("/resource")
                || uri.contains("/templates")
                || uri.contains("/register")
                || uri.contains("/js")) {
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetailsImpl userDetails = jwtTokenUtil.decodeToken(jwtToken);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token invalid.");
            } catch (JwtException e) {
                System.out.println("Unable to get JWT Token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token invalid.");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token invalid.");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token invalid.");
        }

        chain.doFilter(request, response);
    }

}
