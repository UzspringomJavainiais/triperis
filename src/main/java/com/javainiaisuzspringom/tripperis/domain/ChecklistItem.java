package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "checklist_item")
public class ChecklistItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_CHECKED")
    private boolean isChecked;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
