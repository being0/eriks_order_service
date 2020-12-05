package nl.eriks.assignment.orderservice.service;

import org.springframework.security.core.Authentication;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
public interface AuthenticationHolder {

    Authentication getAuthentication();
}
