package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import com.javainiaisuzspringom.tripperis.dto.entity.TripAttachmentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "trip_attachment")
public class TripAttachment implements ConvertableEntity<Integer, TripAttachmentDTO>, Serializable {
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Override
    public TripAttachmentDTO convertToDTO() {
        TripAttachmentDTO dto = new TripAttachmentDTO();

        dto.setId(getId());
        dto.setFileName(getFileName());
        dto.setExtension(getExtension());
        // dto.setFileData(getFileData());
        dto.setTrip(getTrip());

        return dto;
    }
}
