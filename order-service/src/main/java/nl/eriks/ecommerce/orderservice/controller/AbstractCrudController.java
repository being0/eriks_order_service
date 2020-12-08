package nl.eriks.ecommerce.orderservice.controller;

import nl.eriks.ecommerce.orderservice.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Provides REST CRUD API for the order-service.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 5/3/18
 */
public abstract class AbstractCrudController<TO, ID, SERVICE extends CrudService<TO, ID>> {

    @Autowired
    protected SERVICE service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TO create(HttpServletRequest request, @RequestBody TO dto) {

        return service.create(prepareForCreate(dto, request));
    }

    protected Long getIdFromPath(String idInPath, HttpServletRequest request) {

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.valueOf((String) pathVariables.get(idInPath));
    }

    protected TO prepareForCreate(TO to, HttpServletRequest request) {

        return to;
    }

    @GetMapping("/{id}")
    public TO get(@PathVariable("id") ID id) {

        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") ID id) {

        service.delete(id);
    }


}
