package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.services.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ChecklistItemController {

    @Autowired
    private ChecklistItemService checklistItemService;

    @GetMapping("/api/checklist-item")
    public List<ChecklistItemDTO> getAllChecklistItems() {
        return checklistItemService.getAll();
    }

    @PostMapping("/api/checklist-item")
    public ResponseEntity<ChecklistItemDTO> addChecklistItem(@RequestBody ChecklistItemDTO checklistItem) {
        ChecklistItemDTO savedEntity = checklistItemService.save(checklistItem);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
