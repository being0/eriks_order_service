package nl.eriks.ecommerce.orderservice.controller

import nl.eriks.ecommerce.orderservice.service.OrderNotFoundException
import nl.eriks.ecommerce.orderservice.service.OrderService
import nl.eriks.ecommerce.orderservice.service.model.ValidationException
import nl.eriks.ecommerce.orderservice.to.OrderTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class OrderControllerSpec extends Specification {

    MockMvc mockMvc
    OrderService orderService

    def setup() {
        orderService = Stub()
        OrderController orderController = new OrderController()
        orderController.service = orderService
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build()
    }

    def '"GET /api/1/orders/{id}" normal behavior'() {
        given:
        Long id = 12L
        Date creationDate = new Date()
        Date modifiedDate = new Date(creationDate.getTime() + 100000L)
        orderService.get(id) >> new OrderTo(id, OrderTo.OrderStatusTo.created, 101.21 as BigDecimal, creationDate, modifiedDate, "valid_user")

        when:
        def response = mockMvc.perform(get("/api/1/orders/" + id))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("price").value(101.21))
                .andExpect(jsonPath("created").value(creationDate.getTime()))
                .andExpect(jsonPath("modified").value(modifiedDate.getTime()))
                .andExpect(jsonPath("status").value(OrderTo.OrderStatusTo.created.name()))
                .andExpect(jsonPath("user_id").value("valid_user"))
    }

    def '"GET /api/1/orders/{id}" when OrderNotFoundException then expect 404'() {
        given:
        Long id = 12L
        orderService.get(id) >> { ID ->
            throw new OrderNotFoundException("No order found for the specified id!")
        }

        when:
        def response = mockMvc.perform(get("/api/1/orders/" + id))

        then:
        response.andExpect(status().isNotFound())
    }

    def '"POST /api/1/orders" normal behavior'() {
        given:
        Long id = 12L
        Date creationDate = new Date()
        orderService.create(_) >> { OrderTo orderTo ->
            new OrderTo(id, OrderTo.OrderStatusTo.created, orderTo.price, creationDate, creationDate, "valid_user")
        }

        when:
        def response = mockMvc.perform(post("/api/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\":101.21}")
        )

        then:
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("status").value(OrderTo.OrderStatusTo.created.name()))
                .andExpect(jsonPath("price").value(101.21))
                .andExpect(jsonPath("created").value(creationDate.getTime()))
                .andExpect(jsonPath("modified").value(creationDate.getTime()))
                .andExpect(jsonPath("user_id").value("valid_user"))
    }

    def '"POST /api/1/orders" when ValidationException then expect 400'() {
        given:
        orderService.create(_) >> { OrderTo orderTo ->
            throw new ValidationException("Price should not be minus number!")
        }

        when:
        def response = mockMvc.perform(post("/api/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\":-101.21}")
        )

        then:
        response.andExpect(status().isBadRequest())
    }

    def '"PUT /api/1/orders/{id}/cancel" normal behavior should change status to canceled'() {
        given:
        Long id = 12L
        Date creationDate = new Date()
        Date modifiedDate = new Date(creationDate.getTime() + 100000L)
        orderService.cancelOrder(id) >> new OrderTo(id, OrderTo.OrderStatusTo.canceled, 101.21 as BigDecimal, creationDate, modifiedDate, "valid_user")

        when:
        def response = mockMvc.perform(put("/api/1/orders/" + id + "/cancel"))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("status").value(OrderTo.OrderStatusTo.canceled.name()))
                .andExpect(jsonPath("price").value(101.21))
                .andExpect(jsonPath("created").value(creationDate.getTime()))
                .andExpect(jsonPath("modified").value(modifiedDate.getTime()))
                .andExpect(jsonPath("user_id").value("valid_user"))
    }

    def '"PUT /api/1/orders/{id}/cancel" when ObjectNotFound then expect 400'() {
        given:
        Long id = 12L
        orderService.cancelOrder(id) >> { ID ->
            throw new OrderNotFoundException("No order found for the specified id!")
        }

        when:
        def response = mockMvc.perform(put("/api/1/orders/" + id + "/cancel"))

        then:
        response.andExpect(status().isNotFound())
    }

    def '"DELETE /api/1/orders/{id}" normal behavior'() {
        given:
        Long id = 12L
        Date creationDate = new Date()
        Date modifiedDate = new Date(creationDate.getTime() + 100000L)
        orderService.delete(id) >> new OrderTo(id, OrderTo.OrderStatusTo.deleted, 101.21 as BigDecimal, creationDate, modifiedDate, "valid_user")

        when:
        def response = mockMvc.perform(delete("/api/1/orders/" + id))

        then:
        response.andExpect(status().isOk())
    }

    def '"DELETE /api/1/orders/{id}" when ObjectNotFound then expect 400'() {
        given:
        Long id = 12L
        orderService.delete(id) >> { ID ->
            throw new OrderNotFoundException("No order found for the specified id!")
        }

        when:
        def response = mockMvc.perform(delete("/api/1/orders/" + id))

        then:
        response.andExpect(status().isNotFound())
    }

}
