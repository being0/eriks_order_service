package nl.eriks.assignment.orderservice.service

import nl.eriks.assignment.orderservice.service.event.OrderAcceptedEvent
import nl.eriks.assignment.orderservice.service.event.OrderCanceledEvent
import nl.eriks.assignment.orderservice.service.event.OrderCreatedEvent
import nl.eriks.assignment.orderservice.service.event.OrderDeletedEvent
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
    String validUserId = "valid_user_id"

    def setup() {
        orderMapper = new OrderMapper();// a little integration is okay
        orderRepository = Mock()
        orderEventPublisher = Mock(OrderEventPublisher)
        jwtHolder = Mock(JwtHolder)
        jwt = Mock(Jwt)

        jwt.getSubject() >> validUserId
        jwtHolder.getJwt() >> jwt

        // Default behavior
        orderRepository.save(_) >> { Order order -> order }

        orderService = new DefaultOrderService()
        orderService.orderRepository = orderRepository
        orderService.orderMapper = orderMapper
        orderService.orderEventPublisher = orderEventPublisher
        orderService.jwtHolder = jwtHolder
    }

    def 'Create should save on repository and publish OrderCreatedEvent event'() {

        given:
        OrderTo orderTo = new OrderTo(null, null, 121234001.87, null, null, null)
        OrderRepository orderRepository = Mock()
        orderRepository.save(_) >> { Order order ->
            order.id = 100
            return order
        }
        orderService.orderRepository = orderRepository

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

    def '"get" normal behavior should return values from repository'() {

        given:
        // Illegal access
        orderRepository.findById(1L) >> Optional.of(new Order(1L, Order.OrderStatus.created, 100.1 as BigDecimal, new Date(), new Date(), validUserId))

        when:
        OrderTo orderTo = orderService.get(1L)

        then:
        orderTo.id == 1L
        orderTo.status == OrderTo.OrderStatusTo.created
        orderTo.price == 100.1 as BigDecimal

    }

    def '"get" should throw OrderNotFoundException in case object is not found or an illegal access occurs with the same messages'() {

        given:
        // Real not found case
        orderRepository.findById(100L) >> Optional.empty()
        // Illegal access
        orderRepository.findById(5L) >> Optional.of(new Order(5L, null, null, null, null, "another_user"))

        when:
        orderService.get(100L)

        then:
        OrderNotFoundException notFound = thrown(OrderNotFoundException)

        when:
        orderService.get(5L)

        then:
        OrderNotFoundException illegalAccess = thrown(OrderNotFoundException)

        then:
        notFound.message == illegalAccess.message

    }

    def '"cancel" should throw OrderNotFoundException in case object is not found or an illegal access occurs with the same messages'() {

        given:
        // Real not found case
        orderRepository.findById(100L) >> Optional.empty()
        // Illegal access
        orderRepository.findById(5L) >> Optional.of(new Order(5L, null, null, null, null, "another_user"))

        when:
        orderService.cancelOrder(100L)

        then:
        OrderNotFoundException notFound = thrown(OrderNotFoundException)

        when:
        orderService.cancelOrder(5L)

        then:
        OrderNotFoundException illegalAccess = thrown(OrderNotFoundException)

        then:
        notFound.message == illegalAccess.message

    }

    def '"accept" should throw OrderNotFoundException in case object is not found'() {

        given:
        // Real not found case
        orderRepository.findById(100L) >> Optional.empty()

        when:
        orderService.acceptOrder(100L)

        then:
        thrown(OrderNotFoundException)
    }

    def '"delete" should throw OrderNotFoundException in case object is not found or an illegal access occurs with the same messages'() {

        given:
        // Real not found case
        orderRepository.findById(100L) >> Optional.empty()
        // Illegal access
        orderRepository.findById(5L) >> Optional.of(new Order(5L, null, null, null, null, "another_user"))

        when:
        orderService.delete(100L)

        then:
        OrderNotFoundException notFound = thrown(OrderNotFoundException)

        when:
        orderService.delete(5L)

        then:
        OrderNotFoundException illegalAccess = thrown(OrderNotFoundException)

        then:
        notFound.message == illegalAccess.message

    }

    def '"acceptOrder" normal behavior should accept order and raise OrderAcceptedEvent'() {

        given:
        orderRepository.findById(11L) >> Optional.of(new Order(11L, Order.OrderStatus.created, 100.1 as BigDecimal, new Date(), new Date(), validUserId))

        when:
        OrderTo orderTo = orderService.acceptOrder(11L)

        then:
        orderTo.id == 11L
        orderTo.status == OrderTo.OrderStatusTo.accepted
        orderTo.price == 100.1 as BigDecimal

        then: 'OrderAcceptedEvent is published'
        1 * orderEventPublisher.publish(_) >> { OrderEvent event ->
            assert event instanceof OrderAcceptedEvent
        }

    }

    def '"cancelOrder" normal behavior should cancel order and raise OrderCanceledEvent'() {

        given:
        orderRepository.findById(11L) >> Optional.of(new Order(11L, Order.OrderStatus.created, 100.1 as BigDecimal, new Date(), new Date(), validUserId))

        when:
        OrderTo orderTo = orderService.cancelOrder(11L)

        then:
        orderTo.id == 11L
        orderTo.status == OrderTo.OrderStatusTo.canceled
        orderTo.price == 100.1 as BigDecimal

        then: 'OrderCanceledEvent is published'
        1 * orderEventPublisher.publish(_) >> { OrderEvent event ->
            assert event instanceof OrderCanceledEvent
        }

    }

    def '"delete" normal behavior should delete order and raise OrderDeletedEvent'() {

        given:
        orderRepository.findById(11L) >> Optional.of(new Order(11L, Order.OrderStatus.created, 100.1 as BigDecimal, new Date(), new Date(), validUserId))

        when:
        OrderTo orderTo = orderService.delete(11L)

        then:
        orderTo.id == 11L
        orderTo.status == OrderTo.OrderStatusTo.deleted
        orderTo.price == 100.1 as BigDecimal

        then: 'OrderDeletedEvent is published'
        1 * orderEventPublisher.publish(_) >> { OrderEvent event ->
            assert event instanceof OrderDeletedEvent
        }

    }


}
