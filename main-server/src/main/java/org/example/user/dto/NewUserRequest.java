package org.example.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class NewUserRequest {

    @Size(min = 2, max = 250, message = "поле email должно содержать от 2 до 250 символов")
    String name;

    @Email(message = "email должно соответствовать формату")
    @Size(min = 6, max = 254, message = "поле email должно содержать от 6 до 254 символов")
    String email;
}
