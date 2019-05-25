package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import lombok.Data;

@Data
public class TripAttachmentDTO {
    private Integer id;

    private String fileName;

    private String extension;

    private byte[] fileData;

    private Trip trip;
}
