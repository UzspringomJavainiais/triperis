package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link Account}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    // TODO ignore password on get requests
    private String password;

    @Email
    private String email;

    @Builder.Default
    @NotNull
    private List<Integer> roleIds = new ArrayList<>();

    @Builder.Default
    private List<Integer> tripRequestIds = new ArrayList<>();

    @Builder.Default
    private List<Integer> trips = new ArrayList<>();

    @Builder.Default
    private List<Integer> organizedTrips = new ArrayList<>();
}
