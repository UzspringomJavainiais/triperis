package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "trip")
public class Trip implements Serializable {

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

    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripStep> tripSteps = new ArrayList<>();

}
