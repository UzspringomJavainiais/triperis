package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @OneToOne
    private StatusCode status;

    @ManyToMany
    @JoinTable(name = "trip_account", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "trip_id"))
    private List<Account> accounts = new ArrayList<>();

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<ChecklistItem> items = new ArrayList<>();

}
