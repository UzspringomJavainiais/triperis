package com.javainiaisuzspringom.tripperis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripReservationRequest {

    private String name;

    @Size(max = 2000)
    private String description;

    @NonNull
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateFrom;

    @NonNull
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Helsinki")
    private Timestamp dateTo;

    private List<ChecklistItem> checklistItems = new ArrayList<>();

    private List<Account> accounts = new ArrayList<>();

    private Integer from;

    @NonNull
    private Integer to;
}
