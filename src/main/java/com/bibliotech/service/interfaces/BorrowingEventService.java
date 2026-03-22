package com.bibliotech.service.interfaces;

import com.bibliotech.data.document.BorrowingEventDocument;
import com.bibliotech.data.entity.Borrowing;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BorrowingEventService {
    Mono<BorrowingEventDocument> publishEvent(Borrowing borrowing, String eventType);
    Flux<BorrowingEventDocument> findAll();
    Flux<BorrowingEventDocument> findByUserId(Long userId);
}
