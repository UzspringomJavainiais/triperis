package com.javainiaisuzspringom.tripperis.domain;

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

    @OneToOne
    private Account account;

    @Size(max = 200)
    @Column(name = "ACTION")
    private String action;

    @Override
    public AccessLogDTO convertToDTO() {
        AccessLogDTO dto = new AccessLogDTO();

        dto.setId(getId());
        dto.setDate(getDate());
        dto.setAccount(getAccount());
        dto.setAction(getAction());

        return dto;
    }
}
