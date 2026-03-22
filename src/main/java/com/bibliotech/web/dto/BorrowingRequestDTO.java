package com.bibliotech.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BorrowingRequestDTO {

    @NotNull(message = "Le bookId est obligatoire")
    private Long bookId;

    @NotNull(message = "Le userId est obligatoire")
    private Long userId;
}