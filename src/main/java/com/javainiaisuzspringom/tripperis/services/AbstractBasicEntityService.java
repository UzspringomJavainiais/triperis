package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ConvertableEntity;
import com.javainiaisuzspringom.tripperis.dto.entity.ConvertableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractBasicEntityService<E extends ConvertableEntity<I, D>, D extends ConvertableDTO<I>, I> {

    protected abstract JpaRepository<E, I> getRepository();

    @Transactional(propagation = Propagation.REQUIRED)
    public D save(D checklistItem) {
        E entityFromDTO = getExistingOrConvert(checklistItem);
        E savedItem = getRepository().save(entityFromDTO);
        return savedItem.convertToDTO();
    }

    @Transactional
    public E getExistingOrConvert(D dto) {
        if (dto.getId() != null) {
            Optional<E> maybeTripStep = getRepository().findById(dto.getId());
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

    public List<D> getAll() {
        return getRepository().findAll().stream()
                .map(ConvertableEntity::convertToDTO)
                .collect(Collectors.toList());
    }

    protected abstract E convertToEntity(D dto);
}
