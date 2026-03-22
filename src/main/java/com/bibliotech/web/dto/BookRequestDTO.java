package com.bibliotech.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookRequestDTO {

    @NotBlank(message = "L'ISBN est obligatoire")
    @Size(min = 10, max = 13)
    private String isbn;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotNull
    @Min(value = 0)
    private Integer stockDisponible;

    private Long authorId;
    private List<Long> categoryIds;
}