package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class RoomUsage implements ConvertableEntity<Integer, RoomUsageDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnoreProperties({"trips", "organizedTrips", "tripRequests", "accessLog", "roles"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "room_usage_accounts",
            joinColumns = { @JoinColumn(name = "room_usage_id") },
            inverseJoinColumns = { @JoinColumn(name = "accounts_id") })
    private List<Account> accounts = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private ApartmentUsage apartmentUsage;

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;

    @Override
    public RoomUsageDTO convertToDTO() {
        RoomUsageDTO dto = new RoomUsageDTO();

        dto.setId(this.getId());
        if(this.getRoom() != null) {
            dto.setRoomId(this.getRoom().getId());
        }
        if(this.getAccounts() != null) {
            dto.setAccountIds(this.getAccounts().stream().map(Account::getId).collect(Collectors.toList()));
        }

        return dto;
    }
}
