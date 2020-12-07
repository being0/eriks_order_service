package nl.eriks.assignment.orderservice.service

import nl.eriks.assignment.orderservice.service.event.OrderCreatedEvent
import nl.eriks.assignment.orderservice.service.event.OrderEvent
import nl.eriks.assignment.orderservice.service.event.OrderEventPublisher
import nl.eriks.assignment.orderservice.service.mapper.OrderMapper
import nl.eriks.assignment.orderservice.service.model.Order
import nl.eriks.assignment.orderservice.to.OrderTo
import org.springframework.security.oauth2.jwt.Jwt
import spock.lang.Specification

class DefaultOrderServiceSpec extends Specification {

    DefaultOrderService orderService
    OrderRepository orderRepository
    OrderMapper orderMapper
    OrderEventPublisher orderEventPublisher
    JwtHolder jwtHolder
    Jwt jwt

    def setup() {
        orderMapper = new OrderMapper();// a little integration is okay
        orderRepository = Mock()
        orderEventPublisher = Mock(OrderEventPublisher)
        jwtHolder = Mock(JwtHolder)
        jwt = Mock(Jwt)

        jwt.getSubject() >> "valid_user_id"
        jwtHolder.getJwt() >> jwt

        orderService = new DefaultOrderService()
        orderService.orderRepository = orderRepository
        orderService.orderMapper = orderMapper
        orderService.orderEventPublisher = orderEventPublisher
        orderService.jwtHolder = jwtHolder
    }

    def 'Create should save on repository and publish OrderCreatedEvent event'() {

        given:
        OrderTo orderTo = new OrderTo(null, null, 121234001.87, null, null, null)
        orderRepository.save(_) >> { Order order ->
            order.id = 100
            return order
        }

        when:
        OrderTo createdOrderTo = orderService.create(orderTo)

        then: 'OrderCreatedEvent is published'
        1 * orderEventPublisher.publish(_) >> { OrderEvent event ->
            assert event instanceof OrderCreatedEvent
        }

        then: 'OrderTo is created with an id and the requested price '
        createdOrderTo.id == 100L
        createdOrderTo.status == OrderTo.OrderStatusTo.created
        createdOrderTo.price == 121234001.87 as BigDecimal

    }

}
