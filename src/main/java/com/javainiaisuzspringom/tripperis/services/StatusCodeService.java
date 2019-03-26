package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import com.javainiaisuzspringom.tripperis.repositories.StatusCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatusCodeService {

    @Autowired
    private StatusCodeRepository statusCodeRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public StatusCode save(StatusCode statusCode) {
        return statusCodeRepository.save(statusCode);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StatusCode> getAllStatusCodes() {
        return statusCodeRepository.findAll();
    }
}
