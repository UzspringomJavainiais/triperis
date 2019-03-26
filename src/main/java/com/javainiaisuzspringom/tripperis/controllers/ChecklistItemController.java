package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
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
    public List<ChecklistItem> getAllChecklistItems() {
        return checklistItemService.getAllChecklistItems();
    }

    @PostMapping("/checklist-item")
    public ResponseEntity<ChecklistItem> addChecklistItem(@RequestBody ChecklistItem checklistItem) {
        ChecklistItem savedEntity = checklistItemService.save(checklistItem);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}