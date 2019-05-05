package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link Trip}. Used for data transferring and insures no nasty data mangling happens
 */
public class TripDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String description;

    private Integer statusCode;

    private List<Integer> accounts = new ArrayList<>();

    private List<ChecklistItemDTO> items = new ArrayList<>();

    private List<TripStepDTO> tripSteps = new ArrayList<>();

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

    public TripDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TripDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public TripDTO setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public List<Integer> getAccounts() {
        return accounts;
    }

    public TripDTO setAccounts(List<Integer> accounts) {
        this.accounts = accounts;
        return this;
    }

    public List<ChecklistItemDTO> getItems() {
        return items;
    }

    public TripDTO setItems(List<ChecklistItemDTO> items) {
        this.items = items;
        return this;
    }

    public List<TripStepDTO> getTripSteps() {
        return tripSteps;
    }

    public TripDTO setTripSteps(List<TripStepDTO> tripSteps) {
        this.tripSteps = tripSteps;
        return this;
    }
}
