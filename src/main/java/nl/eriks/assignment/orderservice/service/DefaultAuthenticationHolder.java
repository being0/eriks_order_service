package nl.eriks.assignment.orderservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Component
public class DefaultAuthenticationHolder implements AuthenticationHolder {

    @Override
    public JwtAuthenticationToken getAuthentication() {

        return ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication());
    }
}
