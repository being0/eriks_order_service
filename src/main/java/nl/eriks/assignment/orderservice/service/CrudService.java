package nl.eriks.assignment.orderservice.service;

import javax.validation.Valid;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 2/28/18
 */
public interface CrudService<TO, ID> {

    TO create(@Valid TO to);

    TO get(ID id);

//    TO update(@Valid TO to);

    TO delete(ID id);
}