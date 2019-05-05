package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity

@Table(name = "checklist_item")
public class ChecklistItem implements ConvertableEntity<Integer, ChecklistItemDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_CHECKED")
    private boolean isChecked;

    @Column(name = "PRICE")
    private BigDecimal price;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public ChecklistItemDTO convertToDTO() {
        ChecklistItemDTO dto = new ChecklistItemDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setChecked(this.isChecked());

        return dto;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ChecklistItem setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChecklistItem setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public ChecklistItem setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ChecklistItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Trip getTrip() {
        return trip;
    }

    public ChecklistItem setTrip(Trip trip) {
        this.trip = trip;
        return this;
    }
}
