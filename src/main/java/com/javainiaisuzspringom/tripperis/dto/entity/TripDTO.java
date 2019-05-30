package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStatus;

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

    private TripStatus tripStatus;

    private List<Integer> accounts = new ArrayList<>();

    private List<Integer> organizers = new ArrayList<>();

    private List<ChecklistItemDTO> items = new ArrayList<>();

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


    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public TripDTO setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
        return this;
    }

    public List<Integer> getAccounts() {
        return accounts;
    }

    public TripDTO setAccounts(List<Integer> accounts) {
        this.accounts = accounts;
        return this;
    }

    public List<Integer> getOrganizers() {
        return this.organizers;
    }

    public TripDTO setOrganizers(List<Integer> organizers) {
        this.organizers = organizers;
        return this;
    }

    public List<ChecklistItemDTO> getItems() {
        return items;
    }

    public TripDTO setItems(List<ChecklistItemDTO> items) {
        this.items = items;
        return this;
    }

}
