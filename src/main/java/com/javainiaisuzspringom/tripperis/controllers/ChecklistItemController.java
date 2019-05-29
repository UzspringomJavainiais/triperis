package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Attachment;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.AttachmentDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.repositories.AttachmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import com.javainiaisuzspringom.tripperis.services.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ChecklistItemController {
    @Autowired
    private ChecklistItemService checklistItemService;
    @Autowired
    private ChecklistItemRepository checklistItemRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @GetMapping("/api/checklist-item")
    public List<ChecklistItemDTO> getAllChecklistItems() {
        return checklistItemRepository.findAll().stream()
                .map(ChecklistItem::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/checklist-item/{id}")
    public ResponseEntity<ChecklistItemDTO> getChecklistItemById(@PathVariable Integer id) {
        Optional<ChecklistItem> maybeItem = checklistItemRepository.findById(id);

        if (!maybeItem.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(maybeItem.get().convertToDTO());
    }

    @PostMapping("/api/checklist-item")
    public ResponseEntity<ChecklistItemDTO> addChecklistItem(@RequestBody ChecklistItemDTO checklistItem) {
        ChecklistItem savedEntity = checklistItemService.save(checklistItem);
        return new ResponseEntity<>(savedEntity.convertToDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/api/checklist-item/{id}/addFile")
    public ResponseEntity<ChecklistItem> addFileToTrip(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        Optional<ChecklistItem> maybeItem = checklistItemRepository.findById(id);

        if (!maybeItem.isPresent())
            return ResponseEntity.notFound().build();

        Attachment attachment = new Attachment();
        ChecklistItem item = maybeItem.get();

        attachment.setFileName(file.getOriginalFilename().split("\\.")[0]);

        // Extract extension from filename
        if (file.getOriginalFilename().split("\\.").length > 1)
            attachment.setExtension(file.getOriginalFilename().split("\\.")[1]);

        try {
            attachment.setFileData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        attachment = attachmentRepository.save(attachment);

        item.setAttachment(attachment);
        checklistItemRepository.save(item);

        // Dont send the file data back
        attachment.setFileData(null);

        return new ResponseEntity<>(maybeItem.get(), HttpStatus.OK);
    }

    @GetMapping("/api/checklist-item/{id}/attachment")
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable Integer id) {
        Optional<ChecklistItem> maybeItem = checklistItemRepository.findById(id);

        if (!maybeItem.isPresent())
            return ResponseEntity.notFound().build();

        ChecklistItem item = maybeItem.get();

        return new ResponseEntity<>(item.getAttachment().convertToDTO(), HttpStatus.OK);
    }
}
