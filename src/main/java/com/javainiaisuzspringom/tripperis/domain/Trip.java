package com.javainiaisuzspringom.tripperis.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    @OneToOne
    private StatusCode status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "trip_users",
            joinColumns = @JoinColumn(name = "trips_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private Set<User> users;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items;

    public void addChecklistItem(ChecklistItem item) {
        items.add(item);
        item.setTrip(this);
    }

    public void removeChecklistItem(ChecklistItem item) {
        items.remove(item);
        item.setTrip(null);
    }

    public void addUser(User user) {
        users.add(user);
        user.getTrips().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getTrips().remove(this);
    }
}
