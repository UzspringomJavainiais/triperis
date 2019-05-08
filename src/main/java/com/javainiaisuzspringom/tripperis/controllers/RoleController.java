package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import com.javainiaisuzspringom.tripperis.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/api/role")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAll().stream()
                .map(Role::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/role")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO role) {
        Role savedEntity = roleService.save(role);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
