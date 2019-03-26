package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Attachments;
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
    public Attachments save(Attachments attachment) {
        return attachmentRepository.save(attachment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Attachments> getAllAttachments() {
        return attachmentRepository.findAll();
    }
}
