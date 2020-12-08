package nl.eriks.ecommerce.orderservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Component
public class DefaultJwtHolder implements JwtHolder {

    /**
     * Provides JWT from spring security context
     *
     * @return JWT token
     */
    @Override
    public Jwt getJwt() {

        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getToken();
    }
}
