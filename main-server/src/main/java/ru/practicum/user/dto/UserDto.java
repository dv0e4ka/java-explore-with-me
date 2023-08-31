package org.example.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private Long id;

    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "поле email должно содержать от 6 до 254 символов")
    private String email;

    @NotBlank
    @Size(min = 2, max = 250, message = "поле email должно содержать от 2 до 250 символов")
    private String name;
}
