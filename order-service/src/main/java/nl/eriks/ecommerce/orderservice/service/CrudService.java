package nl.eriks.ecommerce.orderservice.service;

import nl.eriks.ecommerce.orderservice.service.model.ValidationException;

import javax.validation.Valid;

/**
 * Interface that abstract basic crud operations.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 2/28/18
 */
public interface CrudService<TO, ID> {

    /**
     * Creates a transferred object
     *
     * @param to Transferred object
     * @return Created transferred object
     */
    TO create(@Valid TO to) throws ValidationException;

    /**
     * Gets a transferred object by its id
     *
     * @param id id of transferred object
     * @return Found transferred object
     */
    TO get(ID id) throws OrderNotFoundException;

//    TO update(@Valid TO to);

    /**
     * Deletes a transferred object by its id
     *
     * @param id id of transferred object
     * @return Deleted transferred object
     */
    TO delete(ID id) throws OrderNotFoundException;
}