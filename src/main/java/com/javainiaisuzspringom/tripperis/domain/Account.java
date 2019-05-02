package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity(name = "Account")
@Getter
@Setter
@Table(name = "account")
public class Account implements ConvertableEntity<Integer, AccountDTO>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Size(max = 100)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Role> roles = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "accounts")
    private List<Trip> trips = new ArrayList<>();

    public AccountDTO convertToDTO() {
        AccountDTO dto = new AccountDTO();

        dto.setId(this.getId());
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setEmail(this.getEmail());
        dto.setPassword(this.getPassword());
        dto.setRoleIds(this.getRoles().stream().map(Role::getId).collect(Collectors.toList()));

        return dto;
    }
}
