package pe.ibk.cpe.dependencies.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.ibk.cpe.dependencies.common.exception.DependencyException;
import pe.ibk.cpe.dependencies.common.exception.error.UserError;
import pe.ibk.cpe.dependencies.common.security.SystemUserData;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;
import pe.ibk.cpe.dependencies.infrastructure.security.token.TokenValidationService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PrivateAppTokenFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final TokenValidationService tokenValidationService;
    private final JsonUtil jsonUtil;

    public PrivateAppTokenFilter(TokenValidationService tokenValidationService, JsonUtil jsonUtil) {
        this.tokenValidationService = tokenValidationService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("PrivateAppFilter ...");
        try {
            TokenValidationService.TokenValidationResponse tokenValidationResponse = validate(request, response);

            List<GrantedAuthority> authorities = tokenValidationResponse.getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            SystemUserData coreAuthenticatedUser = new SystemUserData(tokenValidationResponse.getCredentialId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(coreAuthenticatedUser);

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
        String token = tokenHeader.substring(BEARER.length());

        TokenValidationService.TokenValidationRequest tokenValidationRequest = TokenValidationService.TokenValidationRequest.builder()
                .token(token)
                .build();

        return tokenValidationService.validate(tokenValidationRequest);
    }

}