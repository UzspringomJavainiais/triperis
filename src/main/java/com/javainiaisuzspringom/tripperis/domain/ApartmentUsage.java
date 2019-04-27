package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @ManyToMany
    private List<User> users;

    @ManyToOne
    private Apartment apartment;
}
