package nl.eriks.assignment.orderservice.controller;

import nl.eriks.assignment.orderservice.service.OrderService;
import nl.eriks.assignment.orderservice.to.OrderTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@RestController
@RequestMapping(value = "/api/1/orders")
public class OrderController extends AbstractCrudController<OrderTo, Long, OrderService> {


}
