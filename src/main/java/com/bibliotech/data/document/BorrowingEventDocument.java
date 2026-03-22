package com.bibliotech.data.document;

import com.bibliotech.data.entity.BorrowingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "borrowing_events")
public class BorrowingEventDocument {
    @Id
    private String id;
    private Long borrowingId;
    private Long bookId;
    private Long userId;
    private BorrowingStatus status;
    private String eventType;
    private LocalDateTime eventDate;
}
