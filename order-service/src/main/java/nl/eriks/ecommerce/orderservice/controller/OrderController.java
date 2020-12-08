package nl.eriks.ecommerce.orderservice.controller;

import nl.eriks.ecommerce.orderservice.service.OrderService;
import nl.eriks.ecommerce.orderservice.to.OrderTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@RestController
@RequestMapping(value = "/api/1/orders")
public class OrderController extends AbstractCrudController<OrderTo, Long, OrderService> {

    @PutMapping("/{id}/cancel")
    public OrderTo cancel(@PathVariable("id") Long id) {

        return service.cancelOrder(id);
    }

}
