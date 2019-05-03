package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import org.springframework.stereotype.Service;

@Service
public class ChecklistItemService extends AbstractBasicEntityService<ChecklistItem, ChecklistItemDTO, Integer> {

    protected ChecklistItem convertToEntity(ChecklistItemDTO dto) {
        ChecklistItem item = new ChecklistItem();

        item.setName(dto.getName());
        item.setChecked(dto.isChecked());

        return item;
    }
}
