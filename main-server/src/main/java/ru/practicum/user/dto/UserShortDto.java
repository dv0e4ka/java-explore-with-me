package org.example.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserShortDto {

    @NotNull
    private long id;

    @NotNull
    private String name;
}
