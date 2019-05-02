package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public ChecklistItemDTO save(ChecklistItemDTO checklistItem) {
        ChecklistItem entityFromDTO = getExistingOrConvert(checklistItem);
        ChecklistItem savedItem = checklistItemRepository.save(entityFromDTO);
        return savedItem.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ChecklistItemDTO> getAllChecklistItems() {
        return checklistItemRepository.findAll().stream().map(ChecklistItem::convertToDTO).collect(Collectors.toList());
    }

    // TODO move this method to some utility class, so it doesn't duplicate itself in all services
    @Transactional
    public ChecklistItem getExistingOrConvert(ChecklistItemDTO dto) {
        if (dto.getId() != null) {
            Optional<ChecklistItem> maybeTripStep = checklistItemRepository.findById(dto.getId());
            if (maybeTripStep.isPresent()) {
                return maybeTripStep.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private ChecklistItem convertToEntity(ChecklistItemDTO dto) {
        ChecklistItem item = new ChecklistItem();

//        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setChecked(dto.isChecked());

        return item;
    }
}
