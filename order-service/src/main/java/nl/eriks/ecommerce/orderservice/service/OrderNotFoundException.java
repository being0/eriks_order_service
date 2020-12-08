package nl.eriks.ecommerce.orderservice.service;

import nl.eriks.ecommerce.orderservice.service.model.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
