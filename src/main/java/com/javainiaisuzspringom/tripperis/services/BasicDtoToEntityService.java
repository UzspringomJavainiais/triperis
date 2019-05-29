package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ConvertableEntity;
import com.javainiaisuzspringom.tripperis.dto.entity.ConvertableDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BasicDtoToEntityService<E extends ConvertableEntity<I, D>, D extends ConvertableDTO<I>, I> {

    JpaRepository<E, I> getRepository();

    @Transactional(propagation = Propagation.REQUIRED)
    default E save(D entityDto) {
        E entityFromDTO = getExistingOrConvert(entityDto);
        return getRepository().save(entityFromDTO);
    }

    @Transactional
    default E getExistingOrConvert(D dto) {
        if (dto.getId() != null) {
            Optional<E> optionalEntity = getRepository().findById(dto.getId());
            if (optionalEntity.isPresent()) {
                return optionalEntity.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    E convertToEntity(D dto);
}
