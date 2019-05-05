package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import com.javainiaisuzspringom.tripperis.dto.entity.StatusCodeDTO;
import com.javainiaisuzspringom.tripperis.repositories.StatusCodeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusCodeService extends AbstractBasicEntityService<StatusCode, StatusCodeDTO, Integer> {

    @Getter
    @Autowired
    private StatusCodeRepository repository;

    protected StatusCode convertToEntity(StatusCodeDTO dto) {
        StatusCode statusCode = new StatusCode();

        statusCode.setName(dto.getName());
        statusCode.setDescription(dto.getDescription());

        return statusCode;
    }
}
