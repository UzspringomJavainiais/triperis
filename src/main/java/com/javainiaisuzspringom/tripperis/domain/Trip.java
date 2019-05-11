package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "trip")
public class Trip implements ConvertableEntity<Integer, TripDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @OneToOne
    private StatusCode status;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "trip_account",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "trip_organizers",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> organizers = new ArrayList<>();

    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripStep> tripSteps = new ArrayList<>();

    public TripDTO convertToDTO() {
        TripDTO trip = new TripDTO();

        trip.setId(this.getId());
        trip.setName(this.getName());
        trip.setDescription(this.getDescription());
        if(this.getStatus() != null) {
            trip.setStatusCode(this.getStatus().getId());
        }
        if(this.getAccounts() != null) {
            trip.setAccounts(this.getAccounts().stream().map(Account::getId).collect(Collectors.toList()));
        }
        if(this.getOrganizers() != null) {
            trip.setOrganizers(this.getOrganizers().stream().map(Account::getId).collect(Collectors.toList()));
        }
        if(this.getItems() != null) {
            trip.setItems(this.getItems().stream().map(ChecklistItem::convertToDTO).collect(Collectors.toList()));
        }
        if(this.getTripSteps() != null) {
            trip.setTripSteps(this.getTripSteps().stream().map(TripStep::convertToDTO).collect(Collectors.toList()));
        }

        return trip;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Trip setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Trip setName(String name) {
        this.name = name;
        return this;
    }

    public StatusCode getStatus() {
        return status;
    }

    public Trip setStatus(StatusCode status) {
        this.status = status;
        return this;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Trip setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Trip setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<ChecklistItem> getItems() {
        return items;
    }

    public Trip setItems(List<ChecklistItem> items) {
        this.items = items;
        return this;
    }

    public List<TripStep> getTripSteps() {
        return tripSteps;
    }

    public Trip setTripSteps(List<TripStep> tripSteps) {
        this.tripSteps = tripSteps;
        return this;
    }
}
