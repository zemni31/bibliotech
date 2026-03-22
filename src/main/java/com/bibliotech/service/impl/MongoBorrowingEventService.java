package com.bibliotech.service.impl;

import com.bibliotech.data.document.BorrowingEventDocument;
import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.data.repository.mongo.BorrowingEventRepository;
import com.bibliotech.service.interfaces.BorrowingEventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
@Profile("mongo")
public class MongoBorrowingEventService implements BorrowingEventService {

    private final BorrowingEventRepository borrowingEventRepository;

    public MongoBorrowingEventService(BorrowingEventRepository borrowingEventRepository) {
        this.borrowingEventRepository = borrowingEventRepository;
    }

    @Override
    public Mono<BorrowingEventDocument> publishEvent(Borrowing borrowing, String eventType) {
        BorrowingEventDocument event = BorrowingEventDocument.builder()
                .borrowingId(borrowing.getId())
                .bookId(borrowing.getBook().getId())
                .userId(borrowing.getUserId())
                .status(borrowing.getStatus())
                .eventType(eventType)
                .eventDate(LocalDateTime.now())
                .build();
        return borrowingEventRepository.save(event);
    }

    @Override
    public Flux<BorrowingEventDocument> findAll() {
        return borrowingEventRepository.findAll();
    }

    @Override
    public Flux<BorrowingEventDocument> findByUserId(Long userId) {
        return borrowingEventRepository.findByUserId(userId);
    }
}
