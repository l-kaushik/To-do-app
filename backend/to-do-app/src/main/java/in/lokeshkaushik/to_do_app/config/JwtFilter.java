package in.lokeshkaushik.to_do_app.config;

import in.lokeshkaushik.to_do_app.service.JwtService;
import in.lokeshkaushik.to_do_app.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String bearer = "Bearer ";
        String token = null;
        String username = null;

        if(authHeader == null){
            writeErrorResponse(response, "Missing JWT token after 'Bearer'", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if(authHeader.startsWith(bearer)){
            token = authHeader.substring(bearer.length()).trim();
            username = getUsernameSafely(token, response);
            if(username == null) return;
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/users/") || path.equals("/api/users/login");
    }

    private String getUsernameSafely(String token, HttpServletResponse response) throws IOException {
        try{
            return jwtService.extractUserName(token);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            writeErrorResponse(response, "Invalid JWT signature", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            writeErrorResponse(response, "JWT token expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            writeErrorResponse(response, "Malformed JWT token", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            writeErrorResponse(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
        }
        return null;
    }

    private void writeErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
