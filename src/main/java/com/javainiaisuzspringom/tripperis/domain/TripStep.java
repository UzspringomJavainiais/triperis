package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javainiaisuzspringom.tripperis.dto.entity.TripStepDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "trip_step", indexes = {
        @Index(columnList = "START_DATE"),
        @Index(columnList = "END_DATE")
})
public class TripStep implements ConvertableEntity<Integer, TripStepDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "START_DATE")
    private Timestamp startDate;

    @Column(name = "END_DATE")
    private Timestamp endDate;

    @OneToOne
    private Location location;

    public TripStepDTO convertToDTO() {
        TripStepDTO tripStep = new TripStepDTO();

        tripStep.setId(this.getId());
        tripStep.setStartDate(this.getStartDate());
        tripStep.setEndDate(this.getEndDate());
        tripStep.setOrderNo(this.getOrderNo());
        tripStep.setName(this.getName());
        tripStep.setLocation(this.getLocation().convertToDTO());

        return tripStep;
    }
}
