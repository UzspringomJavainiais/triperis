package com.javainiaisuzspringom.tripperis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javainiaisuzspringom.tripperis.dto.entity.AccessLogDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "access_log")
public class AccessLog implements ConvertableEntity<Integer, AccessLogDTO>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DATE")
    private Timestamp date;

    @Size(max = 20)
    @Column(name = "TYPE")
    private String type;

    @Size(max = 200)
    @Column(name = "ACTION")
    private String action;

    @Size(max = 200)
    @Column(name = "ip_address")
    private String ipAddress;

    @Size(max = 200)
    @Column(name = "requestor_info")
    private String requestorInfo;

    @JsonIgnoreProperties({"trips", "roles", "organizedTrips", "tripRequests", "accessLog"})
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public AccessLogDTO convertToDTO() {
        AccessLogDTO dto = new AccessLogDTO();

        dto.setId(getId());
        dto.setDate(getDate());
        dto.setAccount(getAccount().convertToDTO());
        dto.setType(getType());
        dto.setAction(getAction());

        return dto;
    }
}
