package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javainiaisuzspringom.tripperis.dto.entity.ChecklistItemDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "checklist_item")
@JsonIgnoreProperties("trip")
public class ChecklistItem implements ConvertableEntity<Integer, ChecklistItemDTO>, Serializable {
    public ChecklistItem() {
        
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_CHECKED")
    private Boolean checked;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @JsonIgnore
    @OneToOne
    private Attachment attachment;

    public ChecklistItemDTO convertToDTO() {
        ChecklistItemDTO dto = new ChecklistItemDTO();

        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setChecked(this.getChecked());

        if (getAttachment() != null)
            dto.setAttachment(getAttachment().convertToDTO());

        return dto;
    }

    @Override
    public String toString() {
        return "ChecklistItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", checked=" + checked +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChecklistItem that = (ChecklistItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
