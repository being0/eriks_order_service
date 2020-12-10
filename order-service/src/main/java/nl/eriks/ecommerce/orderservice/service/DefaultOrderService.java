package nl.eriks.ecommerce.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import nl.eriks.ecommerce.orderservice.service.event.*;
import nl.eriks.ecommerce.orderservice.service.mapper.OrderMapper;
import nl.eriks.ecommerce.orderservice.service.model.Order;
import nl.eriks.ecommerce.orderservice.to.OrderTo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Default order service that provides basic crud operations.
 * It persist order into injected repository and publish relevant events for crud operations.
 *
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
    @Resource
    private JwtHolder jwtHolder;

    @Override
    public OrderTo create(@Valid OrderTo orderTo) {
        if (orderTo == null) throw new IllegalArgumentException("Order should not be null.");
        // Open to address in a real project: we need to consider situations that user tries this method multiple times
        // (it could be because they get error even after the creation, like network problems..)
        // so we should not make duplicate(what ever it means) orders

        // Prepare for creation
        Order order = Order.toCreate(extractUserId(), orderTo.getPrice());

        // Create order on repository
        Order createdOrder = orderRepository.save(order);

        // Publish order created event
        orderEventPublisher.publish(new OrderCreatedEvent(createdOrder));

        return orderMapper.mapToDto(createdOrder);
    }

    @Override
    public OrderTo get(Long id) {

        // Find order
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
        if (id == null) throw new IllegalArgumentException("Order id should not be null");

        // Find order
        Order order = orderRepository.findById(id)
                .orElseThrow(this::objectNotFoundException);

        if (!extractUserId().equals(order.getUserId())) {
            // Raise the same message as when object is not found(Should not raise 403)
            // we should not reveal more info because of security
            // However we need to report this attempt to security service(here we just log it for simplicity, don't pull my leg :) the logger could be configured for security service)
            // I wish to use sort of partitioning based on user, so to prevent such issues and improve isolation(not an extra "and user_id=xxx")
            log.error("User {} tried to access order {}", extractUserId(), id);

            throw objectNotFoundException();
        }

        return order;
    }

    @Override
    public OrderTo cancelOrder(Long orderId) {

        // Find order
        Order order = findOrder(orderId);

        // Set the status canceled, this logic could be quit more complex(Starting a Sega...) and could be rejected in certain cases
        Order cancelReadyOrder = order.toCancel();

        // Cancel order in repository
        Order canceledOrder = orderRepository.save(cancelReadyOrder);

        // Publish cancel order event
        orderEventPublisher.publish(new OrderCanceledEvent(canceledOrder));

        return orderMapper.mapToDto(canceledOrder);
    }


    @Override
    public OrderTo acceptOrder(Long orderId) {

        // This method is called by sega transactions not implemented, so we should not check user principal
        Order order = orderRepository.findById(orderId)
                .orElseThrow(this::objectNotFoundException);

        // Set the status accepted
        Order acceptReadyOrder = order.toAccept();

        // Accept order in repository
        Order acceptedOrder = orderRepository.save(acceptReadyOrder);

        // Publish accept order event
        orderEventPublisher.publish(new OrderAcceptedEvent(acceptedOrder));

        return orderMapper.mapToDto(acceptedOrder);
    }

    private OrderNotFoundException objectNotFoundException() {

        return new OrderNotFoundException("No order exists with the specified id!");
    }

    @Override
    public OrderTo delete(Long id) {

        // I don't use soft delete deliberately because of the problems it has(indexing effects, extra stale entries...)
        // Instead we need to archive the deleted orders in another place
        // For simplicity archive process has not been implemented in this project

        // Find order
        Order order = findOrder(id);

        // Prepare to delete
        Order deleteReadyOrder = order.toDelete();

        // Delete order in repository
        orderRepository.delete(deleteReadyOrder);

        // Publish order deleted event
        orderEventPublisher.publish(new OrderDeletedEvent(deleteReadyOrder));

        return orderMapper.mapToDto(deleteReadyOrder);
    }

    private String extractUserId() {

        // extract user id, in the security configuration JWT has been used and JWT subject is user id
        return jwtHolder.getJwt().getSubject();
    }
}
