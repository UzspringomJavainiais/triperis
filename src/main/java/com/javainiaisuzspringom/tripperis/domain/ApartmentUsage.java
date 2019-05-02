package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentUsageDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "apartment_usage")
public class ApartmentUsage implements ConvertableEntity<Integer, ApartmentUsageDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "from_date")
    private Timestamp from;

    @Column(name = "to_date")
    private Timestamp to;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "apartment_id")
    @JsonBackReference
    private Apartment apartment;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="apartment_usage_account", joinColumns=@JoinColumn(name="account_id"), inverseJoinColumns=@JoinColumn(name="apartment_usage_id"))
    private List<Account> accounts;

    public ApartmentUsageDTO convertToDTO() {
        ApartmentUsageDTO dto = new ApartmentUsageDTO();

        dto.setId(this.getId());
        dto.setFrom(this.getFrom());
        dto.setTo(this.getTo());
        dto.setApartmentId(this.getApartment().getId());
        dto.setAccountIds(this.getAccounts().stream().map(Account::getId).collect(Collectors.toList()));

        return dto;
    }
}
