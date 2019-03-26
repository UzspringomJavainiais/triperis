package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.Attachments;
import com.javainiaisuzspringom.tripperis.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttachmentsController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/attachment")
    public List<Attachments> getAllAttachments() {
        return attachmentService.getAllAttachments();
    }

    @PostMapping("/attachment")
    public ResponseEntity<Attachments> addAttachment(@RequestBody Attachments attachment) {
        Attachments savedEntity = attachmentService.save(attachment);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
}
