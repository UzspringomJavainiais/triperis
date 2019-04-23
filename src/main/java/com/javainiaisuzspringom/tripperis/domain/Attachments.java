package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Attachments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Size(max = 20971520) // 20 MB
    @Column(name = "CONTENT")
    private byte[] content;

    @Column(name = "DATE_CREATED")
    private Timestamp dateCreated;

    @Column(name = "DATE_DELETED")
    private Timestamp dateDeleted;
}
