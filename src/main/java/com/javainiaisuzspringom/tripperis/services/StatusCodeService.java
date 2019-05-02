package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import com.javainiaisuzspringom.tripperis.dto.entity.StatusCodeDTO;
import com.javainiaisuzspringom.tripperis.repositories.StatusCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusCodeService {

    @Autowired
    private StatusCodeRepository statusCodeRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public StatusCodeDTO save(StatusCodeDTO statusCode) {
        StatusCode entityFromDTO = getExistingOrConvert(statusCode);
        StatusCode savedEntity = statusCodeRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StatusCodeDTO> getAllStatusCodes() {
        return statusCodeRepository.findAll().stream()
                .map(StatusCode::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StatusCode getExistingOrConvert(StatusCodeDTO dto) {
        if (dto.getId() != null) {
            Optional<StatusCode> maybeStatusCode = statusCodeRepository.findById(dto.getId());
            if (maybeStatusCode.isPresent()) {
                return maybeStatusCode.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private StatusCode convertToEntity(StatusCodeDTO dto) {
        StatusCode statusCode = new StatusCode();

//        statusCode.setId(this.getId());
        statusCode.setName(dto.getName());
        statusCode.setDescription(dto.getDescription());

        return statusCode;
    }

    public StatusCode getById(Integer statusCode) {
        return statusCodeRepository.getOne(statusCode);
    }
}
