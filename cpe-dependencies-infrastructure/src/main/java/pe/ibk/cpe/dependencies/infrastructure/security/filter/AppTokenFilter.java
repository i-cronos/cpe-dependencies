package pe.ibk.cpe.dependencies.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.token.TokenValidationService;

import java.io.IOException;

@Slf4j
public class AppTokenFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final TokenValidationService tokenValidationService;

    public AppTokenFilter(TokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean success = validate(request, response);

        if (success)
            filterChain.doFilter(request, response);
    }

    private boolean validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenHeader = request.getHeader("Authorization");
        System.out.println("TOKEN HEADER : " + tokenHeader);
        return false;
    }

}
