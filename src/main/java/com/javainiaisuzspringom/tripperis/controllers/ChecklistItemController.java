package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Attachment;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.dto.entity.AttachmentDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.repositories.AttachmentRepository;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.services.AttachmentService;
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
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AttachmentService attachmentService;

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
    public ResponseEntity addChecklistItem(@RequestBody ChecklistItemDTO checklistItem) {
        if (checklistItem.getTripId() == null)
            return ResponseEntity.badRequest().body("No trip id supplied!");

        Optional<Trip> maybeTrip = tripRepository.findById(checklistItem.getTripId());

        if (!maybeTrip.isPresent())
            return ResponseEntity.notFound().build();

        ChecklistItem item = new ChecklistItem();

        item.setName(checklistItem.getName());
        item.setChecked(checklistItem.isChecked());
        item.setPrice(checklistItem.getPrice());
        item.setTrip(maybeTrip.get());

        if (checklistItem.getAttachment() != null)
            item.setAttachment(attachmentService.convertToEntity(checklistItem.getAttachment()));

        item = checklistItemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(item.convertToDTO());
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
