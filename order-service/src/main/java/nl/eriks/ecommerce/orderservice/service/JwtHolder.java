package nl.eriks.ecommerce.orderservice.service;

import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
public interface JwtHolder {

    /**
     * Provide JWT normally from context
     *
     * @return jwt token
     */
    Jwt getJwt();
}
