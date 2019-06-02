package com.javainiaisuzspringom.tripperis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javainiaisuzspringom.tripperis.dto.entity.RoomUsageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripReservationResponse {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Helsinki")
    Timestamp dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Helsinki")
    Timestamp dateTo;
    Integer tripId;
    Integer apartmentId;
    List<RoomUsageDTO> successfulReservations;
    List<Integer> unsuccessfulReservationUserIds;
}
