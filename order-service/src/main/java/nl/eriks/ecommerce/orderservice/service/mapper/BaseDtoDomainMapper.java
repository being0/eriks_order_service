package nl.eriks.ecommerce.orderservice.service.mapper;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 1/10/18
 */
public interface BaseDtoDomainMapper<TO, DOMAIN> {

    default List<DOMAIN> mapToDomainList(final List<TO> toList) {

        return toList == null ? null : toList.stream().map(this::mapToDomain).collect(toList());
    }

    default List<TO> mapToDtoList(final List<DOMAIN> domains) {

        return domains == null ? null : domains.stream().map(this::mapToDto).collect(toList());
    }


    TO mapToDto(DOMAIN domain);

    DOMAIN mapToDomain(TO to);
}
