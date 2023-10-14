package pe.ibk.cpe.dependencies.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.ibk.cpe.dependencies.common.exception.error.UserError;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AppWardenFilter extends OncePerRequestFilter {
    private static final String DEVICE_HEADER = "device-id";
    private final JsonUtil jsonUtil;

    public AppWardenFilter(JsonUtil jsonUtil) {
        super();
        this.jsonUtil = jsonUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Core warden filter...");
        String deviceHeader = request.getHeader(DEVICE_HEADER);

        if (Objects.nonNull(deviceHeader)) {
            filterChain.doFilter(request, response);
        } else {
            log.info("Warden Filter, device id header not found: {}", request.getServletPath());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            UserError userError = UserError.builder()
                    .status(HttpStatus.FORBIDDEN.name())
                    .message("No allowed")
                    .build();

            response.getWriter().write(jsonUtil.toJson(userError));
        }
    }

}
