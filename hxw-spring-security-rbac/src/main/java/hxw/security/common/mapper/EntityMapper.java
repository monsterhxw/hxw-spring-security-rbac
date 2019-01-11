package hxw.security.common.mapper;

import java.util.List;

public interface EntityMapper<D, E> {

    /**
     * DTO to Entity
     */
    E toEntity(D dto);

    /**
     * DTO List to Entity List
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity to Dto
     */
    D toDto(E entity);

    /**
     * Entity List to Dto List
     */
    List<D> toDto(List<E> entityList);

}
