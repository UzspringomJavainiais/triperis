package com.javainiaisuzspringom.tripperis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
public class ReservationInfo {
    Timestamp from;
    Timestamp to;
    int reservations;

    public static class FromComparator implements Comparator<ReservationInfo> {

        @Override
        public int compare(ReservationInfo o1, ReservationInfo o2) {
            return o1.getFrom().compareTo(o2.getFrom());
        }
    }
}