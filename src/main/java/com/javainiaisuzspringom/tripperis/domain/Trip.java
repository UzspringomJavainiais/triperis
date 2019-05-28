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

    @JsonIgnoreProperties({"trip", "fileData"})
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripAttachment> tripAttachments = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Trip)
            return ((Trip)obj).getId().equals(getId());
        else
            return obj.equals(this);
    }
}
