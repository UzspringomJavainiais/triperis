package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Attachment;
import com.javainiaisuzspringom.tripperis.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Attachment save(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }
}
