package com.javainiaisuzspringom.tripperis.domain;

import lombok.Data;

@Data
public class TripRequestPatchDTO {
    private Integer tripRequestId;
    private TripRequestStatus decision;
}
