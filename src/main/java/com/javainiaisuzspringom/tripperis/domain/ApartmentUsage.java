package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class ApartmentUsage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "from_date")
    private Timestamp from;

    @Column(name = "to_date")
    private Timestamp to;

    @ManyToMany
    private List<User> users;

    @JsonIgnore
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @ManyToOne
    private Apartment apartment;
}
