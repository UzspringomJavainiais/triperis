package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.StatusCode;
import com.javainiaisuzspringom.tripperis.dto.entity.StatusCodeDTO;
import com.javainiaisuzspringom.tripperis.services.StatusCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StatusCodeController {

    @Autowired
    private StatusCodeService statusCodeService;

    @GetMapping("/api/status-code")
    public List<StatusCodeDTO> getAllStatusCodes() {
        return statusCodeService.getAll().stream()
                .map(StatusCode::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/status-code")
    public ResponseEntity<StatusCodeDTO> addStatusCode(@RequestBody StatusCodeDTO statusCode) {
        StatusCode savedEntity = statusCodeService.save(statusCode);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
