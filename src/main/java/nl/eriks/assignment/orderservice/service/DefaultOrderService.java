package nl.eriks.assignment.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import nl.eriks.assignment.orderservice.service.event.OrderEventPublisher;
import nl.eriks.assignment.orderservice.service.mapper.OrderMapper;
import nl.eriks.assignment.orderservice.service.model.Order;
import nl.eriks.assignment.orderservice.to.OrderTo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
@Service
@Validated
@Slf4j
public class DefaultOrderService implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private OrderEventPublisher orderEventPublisher;

    @Override
    public OrderTo create(@Valid OrderTo orderTo) {

        Order order = orderMapper.mapToDomain(orderTo);

        // Prepare for creation
        order.toCreate(extractJwt().getSubject());

        // Create order
        OrderTo createdOrder = orderMapper.mapToDto(orderRepository.save(order));

        // Publish order created event
        orderEventPublisher.publish(order.createEvent());

        return createdOrder;
    }

    @Override
    public OrderTo get(Long id) {

        Order order = findOrder(id);

        return orderMapper.mapToDto(order);
    }

    /**
     * Finds an order for a user call(User subject/jwt should be in the context).
     *
     * @param id order id
     * @return order
     */
    private Order findOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id + " exists!"));

        if (!extractJwt().getSubject().equals(order.getUserId())) {
            // Raise the same message as when object is not found(Should not raise 403)
            // we should not reveal more info because of security
            // However we need to report this attempt to security service(here we just log it for simplicity, don't pull my leg :) the logger could be configured for security service)
            log.error("User {} tried to access order {}", extractJwt().getSubject(), id);

            throw new OrderNotFoundException("No order with id " + id + " exists!");
        }

        return order;
    }

    @Override
    public OrderTo cancelOrder(Long orderId) {

        Order order = findOrder(orderId);

        // Set the status canceled, this logic could be quit more complex(Starting a Sega...) and could be rejected in certain cases
        order.toCancel();

        // Cancel order in repository
        OrderTo canceledOrder = orderMapper.mapToDto(orderRepository.save(order));

        // Publish cancel order event
        orderEventPublisher.publish(order.createEvent());

        return canceledOrder;
    }


    @Override
    public OrderTo acceptOrder(Long orderId) {

        // This method is called by sega transactions not implemented, so we should not check user principal
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + orderId + " exists!"));

        // Set the status accepted
        order.toAccept();

        // Accept order in repository
        OrderTo acceptedOrder = orderMapper.mapToDto(orderRepository.save(order));

        // Publish accept order event
        orderEventPublisher.publish(order.createEvent());

        return acceptedOrder;
    }

    @Override
    public OrderTo delete(Long id) {

        // I don't use soft delete deliberately because of the problems it has(indexing effects, extra stale entries...)
        // Instead we need to archive the deleted orders in another place
        // For simplicity archive process has not been implemented in this project
        Order order = findOrder(id);

        // Delete order in repository
        orderRepository.delete(order.toDelete());

        // Publish delete order event
        orderEventPublisher.publish(order.createEvent());

        return orderMapper.mapToDto(order);
    }

    private Date getCurrentTime() {

        return new Date();
    }

    private Jwt extractJwt() {

        return null;
    }
}
