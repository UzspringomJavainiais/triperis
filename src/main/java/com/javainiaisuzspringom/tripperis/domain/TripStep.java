package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "trip_step", indexes = {@Index(columnList = "START_DATE"), @Index(columnList = "END_DATE")})
@JsonIgnoreProperties("trip")
public class TripStep implements ConvertableEntity<Integer, TripStepDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "START_DATE")
    private Timestamp startDate;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    @Column(name = "END_DATE")
    private Timestamp endDate;

    @OneToOne
    private Location location;

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;

    public TripStepDTO convertToDTO() {
        TripStepDTO tripStep = new TripStepDTO();

        tripStep.setId(this.getId());
        tripStep.setStartDate(this.getStartDate());
        tripStep.setEndDate(this.getEndDate());
        tripStep.setOrderNo(this.getOrderNo());
        tripStep.setName(this.getName());
        if(this.getLocation() != null) {
            tripStep.setLocation(this.getLocation().convertToDTO());
        }

        return tripStep;
    }

    @Override
    public String toString() {
        return "TripStep{" +
                "id=" + id +
                ", trip=" + trip +
                ", name='" + name + '\'' +
                ", orderNo=" + orderNo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location=" + location +
                '}';
    }
}
