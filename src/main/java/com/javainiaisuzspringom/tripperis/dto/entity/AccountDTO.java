package com.javainiaisuzspringom.tripperis.dto.entity;

import com.javainiaisuzspringom.tripperis.domain.Account;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to {@link Account}
 */
@Data
public class AccountDTO implements ConvertableDTO<Integer>{

    private Integer id;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    private String password;

    @Email
    private String email;

    @NotNull
    private List<Integer> roleIds = new ArrayList<>();
}
