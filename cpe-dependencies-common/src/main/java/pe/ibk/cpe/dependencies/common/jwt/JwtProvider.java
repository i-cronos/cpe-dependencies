package pe.ibk.cpe.dependencies.common.jwt;

import java.util.UUID;

public class JwtProvider {

    public String token(Object object) {
        return UUID.randomUUID().toString();
    }
}
