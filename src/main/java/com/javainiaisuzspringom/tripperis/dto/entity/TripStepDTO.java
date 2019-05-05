package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.TripStep;
import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Maps to {@link TripStep}
 */
public class TripStepDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    private Integer orderNo;

    private Timestamp startDate;

    private Timestamp endDate;

    private LocationDTO location;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TripStepDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public TripStepDTO setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public TripStepDTO setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        return this;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public TripStepDTO setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        return this;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public TripStepDTO setLocation(LocationDTO location) {
        this.location = location;
        return this;
    }
}
