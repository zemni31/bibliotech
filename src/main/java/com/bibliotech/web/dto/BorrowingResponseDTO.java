package com.bibliotech.web.dto;

import com.bibliotech.data.entity.BorrowingStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BorrowingResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private BorrowingStatus status;
    private LocalDateTime createdAt;
}