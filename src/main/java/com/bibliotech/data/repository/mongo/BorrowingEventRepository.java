package com.bibliotech.data.repository.mongo;

import com.bibliotech.data.document.BorrowingEventDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BorrowingEventRepository extends ReactiveMongoRepository<BorrowingEventDocument, String> {
    Flux<BorrowingEventDocument> findByUserId(Long userId);
}
