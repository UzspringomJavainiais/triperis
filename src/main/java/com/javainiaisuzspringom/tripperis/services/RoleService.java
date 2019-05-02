package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.RoleDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO save(RoleDTO role) {
        Role entityFromDTO = getExistingOrConvert(role);
        Role savedEntity = roleRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(Role::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Role getExistingOrConvert(RoleDTO dto) {
        if (dto.getId() != null) {
            Optional<Role> maybeRole = roleRepository.findById(dto.getId());
            if (maybeRole.isPresent()) {
                return maybeRole.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private Role convertToEntity(RoleDTO dto) {
        Role role = new Role();

//        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setDateCreated(dto.getDateCreated());
        role.setDateDeleted(dto.getDateDeleted());

        return role;
    }
}
