package com.bibliotech.web.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookResponseDTO {
    private Long id;
    private String isbn;
    private String title;
    private Integer stockDisponible;
    private String authorName;
    private List<String> categories;
    private LocalDateTime createdAt;
}