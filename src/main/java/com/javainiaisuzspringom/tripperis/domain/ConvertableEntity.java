package com.javainiaisuzspringom.tripperis.domain;

public interface ConvertableEntity<ID, DTO> {
    ID getId();
    DTO convertToDTO();
}
