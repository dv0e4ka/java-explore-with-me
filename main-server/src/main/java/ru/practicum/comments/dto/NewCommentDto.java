package ru.practicum.comments.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewCommentDto {
    @NotBlank
    @Size(min = 2, max = 7000)
    private String text;
}
