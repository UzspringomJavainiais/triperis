package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "trip")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NAME")
    private String name;

    @OneToOne
    private StatusCode status;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateFrom;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateTo;

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests", "accessLog"})
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "trip_account", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts = new ArrayList<>();

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests", "accessLog"})
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "trip_organizers", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> organizers = new ArrayList<>();

    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    @JsonIgnoreProperties("trip")
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripStep> tripSteps = new ArrayList<>();

    @JsonIgnoreProperties("trip")
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripRequest> tripRequests = new ArrayList<>();

    @JsonIgnoreProperties("trip")
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripAttachment> tripAttachments = new ArrayList<>();

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

    public List<Account> getOrganizers() {
        return organizers;
    }

    public Trip setOrganizers(List<Account> organizers) {
        this.organizers = organizers;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Trip setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public Trip setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
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
