package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ConvertableEntity;
import com.javainiaisuzspringom.tripperis.dto.entity.ConvertableDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class AbstractBasicEntityService<E extends ConvertableEntity<I, D>, D extends ConvertableDTO<I>, I> {

    protected abstract JpaRepository<E, I> getRepository();

    @Transactional(propagation = Propagation.REQUIRED)
    public E save(D entityDto) {
        E entityFromDTO = getExistingOrConvert(entityDto);
        return getRepository().save(entityFromDTO);
    }

    @Transactional
    public E getExistingOrConvert(D dto) {
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

    public List<E> getAll() {
        return getRepository().findAll();
    }

    protected abstract E convertToEntity(D dto);
}
