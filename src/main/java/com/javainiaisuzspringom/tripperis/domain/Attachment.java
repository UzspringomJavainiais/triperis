package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.AttachmentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "attachment")
public class Attachment implements ConvertableEntity<Integer, AttachmentDTO>, Serializable {
    public Attachment() {

    }

    public Attachment(Attachment attachment) {
        this.fileName = attachment.fileName;
        this.extension = attachment.extension;
        this.fileData = attachment.fileData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "name")
    private String fileName;

    @Size(max = 10)
    @Column(name = "extension")
    private String extension;

    @Column(name = "file_data")
    private byte[] fileData;

    @Override
    public AttachmentDTO convertToDTO() {
        AttachmentDTO dto = new AttachmentDTO();

        dto.setId(getId());
        dto.setFileName(getFileName());
        dto.setExtension(getExtension());
        // dto.setFileData(getFileData());

        return dto;
    }
}
