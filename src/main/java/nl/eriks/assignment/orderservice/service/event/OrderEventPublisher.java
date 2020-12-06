package nl.eriks.assignment.orderservice.service.event;

/**
 * Publishes order events
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
public interface OrderEventPublisher {

    void publish(OrderEvent orderEvent);
}
