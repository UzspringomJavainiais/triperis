package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import com.javainiaisuzspringom.tripperis.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/role")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAll();
    }

    @PostMapping("/role")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO role) {
        RoleDTO savedEntity = roleService.save(role);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
