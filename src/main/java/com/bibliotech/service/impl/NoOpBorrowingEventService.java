package com.bibliotech.service.impl;

import com.bibliotech.data.document.BorrowingEventDocument;
import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.service.interfaces.BorrowingEventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Profile("!mongo")
public class NoOpBorrowingEventService implements BorrowingEventService {

    @Override
    public Mono<BorrowingEventDocument> publishEvent(Borrowing borrowing, String eventType) {
        return Mono.empty();
    }

    @Override
    public Flux<BorrowingEventDocument> findAll() {
        return Flux.empty();
    }

    @Override
    public Flux<BorrowingEventDocument> findByUserId(Long userId) {
        return Flux.empty();
    }
}
