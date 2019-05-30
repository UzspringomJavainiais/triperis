package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Attachment;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.repositories.AttachmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChecklistItemService implements BasicDtoToEntityService<ChecklistItem, ChecklistItemDTO, Integer> {

    @Getter
    @Autowired
    private ChecklistItemRepository repository;
    @Autowired
    private AttachmentService attachmentService;

    public ChecklistItem convertToEntity(ChecklistItemDTO dto) {
        ChecklistItem item = new ChecklistItem();

        item.setName(dto.getName());
        item.setChecked(dto.getChecked());
        item.setPrice(dto.getPrice());

        if (dto.getAttachment() != null) {
            item.setAttachment(attachmentService.convertToEntity(dto.getAttachment()));
        }
        item.setChecked(dto.getChecked());

        return item;
    }
}
