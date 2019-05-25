package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Entity(name = "Account")
@Getter
@Setter
@Table(name = "account")
public class Account implements ConvertableEntity<Integer, AccountDTO>, UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "EMAIL", unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @JsonIgnoreProperties("account")
    @ManyToMany(mappedBy = "accounts")
    private List<Trip> trips = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "trip_organizers",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "trip_id")
    )
    private List<Trip> organizedTrips;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripRequest> tripRequests = new ArrayList<>();

    @Version
    @Column(name = "opt_lock_version")
    private Integer optLockVersion;

    public AccountDTO convertToDTO() {
        AccountDTO dto = new AccountDTO();

        dto.setId(this.getId());
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setEmail(this.getEmail());
        dto.setPassword(null);

        if (this.getRoles() != null) {
            dto.setRoleIds(this.getRoles().stream().map(Role::getId).collect(toList()));
        }
//        if (this.getTripRequests() != null) {
//            dto.setTripRequestIds(this.getTripRequests().stream().map(TripRequest::getId).collect(Collectors.toList()));
//        }
        if (this.getTrips() != null) {
            dto.setTrips(this.getTrips().stream().map(Trip::getId).collect(Collectors.toList()));
        }
        if (this.getOrganizedTrips() != null) {
            dto.setOrganizedTrips(this.getOrganizedTrips().stream().map(Trip::getId).collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // TODO this can be improved when the need arises
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
