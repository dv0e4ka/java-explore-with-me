package ru.practicum.categoriy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long id;

    @Size(min = 1, max = 50, message = "поле name должно содержать от 1 до 50 символов")
    private String name;
}
