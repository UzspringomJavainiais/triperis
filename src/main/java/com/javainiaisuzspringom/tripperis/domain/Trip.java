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
import java.util.Objects;

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

    @Column(name = "trip_status")
    private TripStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Helsinki")
    private Timestamp dateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Helsinki")
    private Timestamp dateTo;

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests", "accessLog"})
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "trip_account", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts = new ArrayList<>();

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests", "accessLog"})
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "trip_organizers", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> organizers = new ArrayList<>();

    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    @JsonIgnoreProperties("trip")
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripRequest> tripRequests = new ArrayList<>();

    @JsonIgnoreProperties("trip")
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<ApartmentUsage> tripApartmentUsages = new ArrayList<>();

/*    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;*/

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

    public TripStatus getStatus() {
        return status;
    }

    public Trip setStatus(TripStatus status) {
        this.status = status;
        return this;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<Account> getOrganizers() {

        return organizers;
    }
}
