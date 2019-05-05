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
@Getter
@Setter
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

<<<<<<< HEAD
    @Column(name = "PRICE")
    private BigDecimal price;

=======
    @JsonBackReference
>>>>>>> 191b73cdca999be97dd935051cf4ed95b5172a51
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
}
