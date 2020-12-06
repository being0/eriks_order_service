package nl.eriks.assignment.orderservice.service.event;

import lombok.Getter;
import lombok.Setter;
import nl.eriks.assignment.orderservice.service.model.Order;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Getter
@Setter
public class OrderCanceledEvent extends AbstractOrderEvent {

    public OrderCanceledEvent(Order order) {
        super(order);
    }
}
