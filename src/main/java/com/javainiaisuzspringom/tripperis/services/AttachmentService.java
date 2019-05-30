package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Attachment;
import com.javainiaisuzspringom.tripperis.dto.entity.AttachmentDTO;
import com.javainiaisuzspringom.tripperis.repositories.AttachmentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService implements BasicDtoToEntityService<Attachment, AttachmentDTO, Integer> {
    @Getter
    @Autowired
    private AttachmentRepository repository;

    @Override
    public Attachment convertToEntity(AttachmentDTO dto) {
        Attachment attachment = new Attachment();

        attachment.setId(dto.getId());
        attachment.setFileData(dto.getFileData());
        attachment.setExtension(dto.getExtension());
        attachment.setFileName(dto.getFileName());

        return attachment;
    }
}
