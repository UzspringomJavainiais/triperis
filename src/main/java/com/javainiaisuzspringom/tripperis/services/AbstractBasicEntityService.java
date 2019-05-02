package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ConvertableEntity;
import com.javainiaisuzspringom.tripperis.dto.entity.ConvertableDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// TODO use this, maybe the generics are a bit over the top
public abstract class AbstractBasicEntityService<E extends ConvertableEntity<I, D>, D extends ConvertableDTO<I>, I> {

    protected JpaRepository<E, I> repository;

    @Transactional(propagation = Propagation.REQUIRED)
    public D save(D checklistItem) {
        E entityFromDTO = getExistingOrConvert(checklistItem);
        E savedItem = repository.save(entityFromDTO);
        return savedItem.convertToDTO();
    }

    @Transactional
    public E getExistingOrConvert(D dto) {
        if (dto.getId() != null) {
            Optional<E> maybeTripStep = repository.findById(dto.getId());
            if (maybeTripStep.isPresent()) {
                return maybeTripStep.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    protected abstract E convertToEntity(D dto);
}
