package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "trip_request")
public class TripRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests"})
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
