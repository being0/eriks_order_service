package nl.eriks.assignment.orderservice.service.mapper;

import nl.eriks.assignment.orderservice.service.model.Order;
import nl.eriks.assignment.orderservice.to.OrderTo;
import org.springframework.stereotype.Component;

/**
 * Maps order model to order transfer object
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
@Component
public class OrderMapper implements BaseDtoDomainMapper<OrderTo, Order> {

    @Override
    public OrderTo mapToDto(Order order) {
        if (order == null) return null;

        return new OrderTo(order.getId(), order.getStatus() == null ? null : OrderTo.OrderStatusTo.valueOf(order.getStatus().name()),
                order.getPrice(), order.getCreated(), order.getModified(), order.getUserId());
    }

    @Override
    public Order mapToDomain(OrderTo orderTo) {
        if (orderTo == null) return null;

        return new Order(orderTo.getId(), orderTo.getStatus() == null ? null : Order.OrderStatus.valueOf(orderTo.getStatus().name()),
                orderTo.getPrice(), orderTo.getCreated(), orderTo.getModified(), orderTo.getUserId());
    }
}
