package nl.eriks.assignment.orderservice.service.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.eriks.assignment.orderservice.service.model.Order;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class AbstractOrderEvent implements OrderEvent {

    protected Long orderId;

    protected String userId;

    protected BigDecimal price;

    protected Date orderCreated;

    protected Date raiseDate;

    protected Order.OrderStatus prevStatus;

    public AbstractOrderEvent(Order order) {
        setOrderCreated(order.getCreated());
        setOrderId(order.getId());
        setPrevStatus(order.getPrevStatus());
        setPrice(order.getPrice());
        setUserId(order.getUserId());
        setRaiseDate(Order.getCurrentTime());
    }
}
