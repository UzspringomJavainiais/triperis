package com.javainiaisuzspringom.tripperis.dto.entity;

import lombok.Data;

@Data
public class AttachmentDTO implements ConvertableDTO<Integer> {
    private Integer id;

    private String fileName;

    private String extension;

    private byte[] fileData;
}
