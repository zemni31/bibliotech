package com.bibliotech.data.repository;

import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.data.entity.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByStatus(BorrowingStatus status);

    List<Borrowing> findByBookId(Long bookId);

    List<Borrowing> findByUserIdAndStatus(Long userId, BorrowingStatus status);

    long countByUserIdAndStatus(Long userId, BorrowingStatus status);

    List<Borrowing> findByStatusAndBorrowDateBefore(BorrowingStatus status, LocalDate date);
    boolean existsByBook_IdAndStatus(Long bookId, BorrowingStatus status);
}
