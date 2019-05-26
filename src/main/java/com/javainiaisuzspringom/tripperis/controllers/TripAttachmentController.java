package com.javainiaisuzspringom.tripperis.controllers;

import com.javainiaisuzspringom.tripperis.domain.TripAttachment;
import com.javainiaisuzspringom.tripperis.repositories.TripAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin
public class TripAttachmentController {
    @Autowired
    private TripAttachmentRepository tripAttachmentRepository;

    @GetMapping("/api/tripAttachment/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id, HttpServletRequest request) {
        Optional<TripAttachment> maybeTripAttachment = tripAttachmentRepository.findById(id);

        if (!maybeTripAttachment.isPresent())
            return ResponseEntity.notFound().build();

        TripAttachment attachment = maybeTripAttachment.get();

        Resource resource = new ByteArrayResource(attachment.getFileData());
        String contentType;

        if (attachment.getFileName() != null && attachment.getExtension() != null)
            contentType = request.getServletContext().getMimeType(attachment.getFileName() + "." + attachment.getExtension());
        else
            contentType = "application/octet-stream"; // fallback to a default content type

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "." + attachment.getExtension() + "\"")
                .body(resource);
    }
}
