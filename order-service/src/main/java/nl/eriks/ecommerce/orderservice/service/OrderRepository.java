package nl.eriks.ecommerce.orderservice.service;

import nl.eriks.ecommerce.orderservice.service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides access to order repository
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
