package com.javainiaisuzspringom.tripperis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.dto.entity.ApartmentDTO;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class TripReservationRequest {

    private String name;

    @Size(max = 2000)
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp from;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp to;

    private List<ChecklistItem> checklistItems = new ArrayList<>();

    private List<Account> accounts = new ArrayList<>();

    @NonNull
    private ApartmentDTO apartmentFrom;

    @NonNull
    private ApartmentDTO apartmentTo;

}
