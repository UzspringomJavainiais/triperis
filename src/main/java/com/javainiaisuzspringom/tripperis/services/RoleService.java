package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.utils.DateUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractBasicEntityService<Role, RoleDTO, Integer> {

    @Getter
    @Autowired
    private RoleRepository repository;

    public RoleDTO saveRole(RoleDTO role) {
        role.setDateCreated(DateUtils.now());
        save(role);
        return role;
    }

    protected Role convertToEntity(RoleDTO dto) {
        Role role = new Role();

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setDateCreated(dto.getDateCreated());
        role.setDateDeleted(dto.getDateDeleted());
        return role;
    }
}
