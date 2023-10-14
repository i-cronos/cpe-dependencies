package pe.ibk.cpe.dependencies.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;
import pe.ibk.cpe.dependencies.infrastructure.security.token.TokenValidationService;

import java.io.IOException;

@Slf4j
public class PrivateAppFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final TokenValidationService tokenValidationService;
    private final JsonUtil jsonUtil;

    public PrivateAppFilter(TokenValidationService tokenValidationService, JsonUtil jsonUtil) {
        this.tokenValidationService = tokenValidationService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("PrivateAppFilter ...");
    }

}
