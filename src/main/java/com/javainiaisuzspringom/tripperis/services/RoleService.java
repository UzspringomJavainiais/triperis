package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractBasicEntityService<Role, RoleDTO, Integer> {

    protected Role convertToEntity(RoleDTO dto) {
        Role role = new Role();

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setDateCreated(dto.getDateCreated());
        role.setDateDeleted(dto.getDateDeleted());

        return role;
    }
}
