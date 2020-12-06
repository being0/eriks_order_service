package nl.eriks.assignment.orderservice.service.mapper

import nl.eriks.assignment.orderservice.service.model.Order
import nl.eriks.assignment.orderservice.to.OrderTo
import spock.lang.Specification

class OrderMapperSpec extends Specification {

    def 'mapToDto boundary null'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        OrderTo orderTo = orderMapper.mapToDto(null);

        then:
        orderTo == null
    }

    def 'mapToDto maps domain to dto'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        OrderTo orderTo = orderMapper.mapToDto(new Order(10L, Order.OrderStatus.created, 100.1 as BigDecimal, new Date(100), new Date(200), "user1000"));

        then:
        orderTo.id == 10L
        orderTo.status == OrderTo.OrderStatusTo.created
        orderTo.price == 100.1 as BigDecimal
        orderTo.created == new Date(100)
        orderTo.modified == new Date(200)
        orderTo.userId == "user1000"
    }

    def 'mapToDto all null'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        OrderTo orderTo = orderMapper.mapToDto(new Order(null, null, null, null, null, null));

        then:
        orderTo.id == null
        orderTo.status == null
        orderTo.price == null
        orderTo.created == null
        orderTo.modified == null
        orderTo.userId == null
    }

    def 'mapToDto with different statuses'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        OrderTo orderTo = orderMapper.mapToDto(new Order(10L, status, 100.1 as BigDecimal, new Date(100), new Date(200), "user1000"));

        then:
        orderTo.status == expectedStatusTo

        where:
        status                     || expectedStatusTo

        null                       || null
        Order.OrderStatus.created  || OrderTo.OrderStatusTo.created
        Order.OrderStatus.accepted || OrderTo.OrderStatusTo.accepted
        Order.OrderStatus.canceled || OrderTo.OrderStatusTo.canceled
        Order.OrderStatus.deleted  || OrderTo.OrderStatusTo.deleted

    }

    def 'mapToDomain boundary null'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        Order order = orderMapper.mapToDomain(null);

        then:
        order == null
    }

    def 'mapToDomain maps dto to domain'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        Order order = orderMapper.mapToDomain(new OrderTo(10L, OrderTo.OrderStatusTo.created, 100.1 as BigDecimal, new Date(100), new Date(200), "user1000"));

        then:
        order.id == 10L
        order.status == Order.OrderStatus.created
        order.price == 100.1 as BigDecimal
        order.created == new Date(100)
        order.modified == new Date(200)
        order.userId == "user1000"
    }

    def 'mapToDomain with different statuses'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        Order order = orderMapper.mapToDomain(new OrderTo(10L, statusTo, 100.1 as BigDecimal, new Date(100), new Date(200), "user1000"));

        then:
        order.status == expectedStatus

        where:
        statusTo                       || expectedStatus

        null                           || null
        OrderTo.OrderStatusTo.created  || Order.OrderStatus.created
        OrderTo.OrderStatusTo.accepted || Order.OrderStatus.accepted
        OrderTo.OrderStatusTo.canceled || Order.OrderStatus.canceled
        OrderTo.OrderStatusTo.deleted  || Order.OrderStatus.deleted
    }

    def 'mapToDomain all null'() {

        given:
        OrderMapper orderMapper = new OrderMapper();

        when:
        Order order = orderMapper.mapToDomain(new OrderTo(null, null, null, null, null, null));

        then:
        order.id == null
        order.status == null
        order.price == null
        order.created == null
        order.modified == null
        order.userId == null
    }


}