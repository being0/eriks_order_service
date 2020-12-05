package nl.eriks.assignment.orderservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Component
public class DefaultAuthenticationHolder implements AuthenticationHolder {

    @Override
    public Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }
}
