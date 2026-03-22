package com.bibliotech.service;

import com.bibliotech.service.interfaces.BorrowingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BorrowingScheduler {

    private static final Logger log = LoggerFactory.getLogger(BorrowingScheduler.class);
    private final BorrowingService borrowingService;

    public BorrowingScheduler(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void markExpiredBorrowingsAsOverdue() {
        int updated = borrowingService.markOverdueBorrowings();
        log.info("BorrowingScheduler - emprunts marques OVERDUE: {}", updated);
    }
}
