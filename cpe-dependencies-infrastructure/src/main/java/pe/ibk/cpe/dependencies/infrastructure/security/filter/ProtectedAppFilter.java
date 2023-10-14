package pe.ibk.cpe.dependencies.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.DependencyException;
import pe.ibk.cpe.dependencies.common.exception.error.UserError;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;
import pe.ibk.cpe.dependencies.infrastructure.security.token.TokenValidationService;

import java.io.IOException;

@Slf4j
public class ProtectedAppFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final TokenValidationService tokenValidationService;
    private final JsonUtil jsonUtil;

    public ProtectedAppFilter(TokenValidationService tokenValidationService, JsonUtil jsonUtil) {
        this.tokenValidationService = tokenValidationService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("ProtectedAppFilter ...");
        try {
            TokenValidationService.TokenValidationResponse tokenValidationResponse = validate(request, response);

            filterChain.doFilter(request, response);
        } catch (DependencyException ex) {
            log.error("Error in token validation : {}", ex.getError());
            UserError error = UserError.builder()
                    .message("Not valid token")
                    .build();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonUtil.toJson(error));

        }
    }

    private TokenValidationService.TokenValidationResponse validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenHeader = request.getHeader("Authorization");
        System.out.println("TOKEN HEADER ::: " + tokenHeader);
        String token = tokenHeader.substring(BEARER.length());
        System.out.println("TOKEN ::: " + token);
        TokenValidationService.TokenValidationRequest tokenValidationRequest = TokenValidationService.TokenValidationRequest.builder()
                .token(token)
                .build();

        return tokenValidationService.validate(tokenValidationRequest);
    }
}
