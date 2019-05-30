package com.javainiaisuzspringom.tripperis.domain;

import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class RoomUsage implements ConvertableEntity<Integer, RoomUsageDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    private List<Account> accounts = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

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
