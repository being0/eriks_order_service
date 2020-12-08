package nl.eriks.ecommerce.orderservice.service.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.eriks.ecommerce.orderservice.service.model.Order;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrderCreatedEvent extends AbstractOrderEvent {

    public OrderCreatedEvent(Order order) {
        super(order);
    }
}
