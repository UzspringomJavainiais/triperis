package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.services.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChecklistItemController {

    @Autowired
    private ChecklistItemService checklistItemService;

    @GetMapping("/checklist-item")
    public List<ChecklistItemDTO> getAllChecklistItems() {
        return checklistItemService.getAll();
    }

    @PostMapping("/checklist-item")
    public ResponseEntity<ChecklistItemDTO> addChecklistItem(@RequestBody ChecklistItemDTO checklistItem) {
        ChecklistItemDTO savedEntity = checklistItemService.save(checklistItem);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
