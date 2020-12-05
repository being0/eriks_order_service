package nl.eriks.assignment.orderservice.service;

import nl.eriks.assignment.orderservice.to.OrderTo;

/**
 * Order service interface
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
public interface OrderService extends CrudService<OrderTo, Long> {

    /**
     * Cancels order
     * @param orderId order id
     * @return order
     */
    OrderTo cancelOrder(Long orderId);

    /**
     * Accept order, this is called by in a sega transaction(not user)
     *
     * @param orderId
     * @return
     */
    OrderTo acceptOrder(Long orderId);
}
