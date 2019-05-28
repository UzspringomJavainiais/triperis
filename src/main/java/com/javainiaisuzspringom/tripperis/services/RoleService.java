package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements BasicDtoToEntityService<Role, RoleDTO, Integer> {

    @Getter
    @Autowired
    private RoleRepository repository;

    public Role convertToEntity(RoleDTO dto) {
        Role role = new Role();

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setDateCreated(dto.getDateCreated());
        role.setDateDeleted(dto.getDateDeleted());
        return role;
    }
}
