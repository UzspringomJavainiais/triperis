package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import com.javainiaisuzspringom.tripperis.services.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ChecklistItemController {

    @Autowired
    private ChecklistItemService checklistItemService;
    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @GetMapping("/api/checklist-item")
    public List<ChecklistItemDTO> getAllChecklistItems() {
        return checklistItemRepository.findAll().stream()
                .map(ChecklistItem::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/checklist-item")
    public ResponseEntity<ChecklistItemDTO> addChecklistItem(@RequestBody ChecklistItemDTO checklistItem) {
        ChecklistItem savedEntity = checklistItemService.save(checklistItem);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }
}
