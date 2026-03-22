package com.bibliotech.service.impl;

import com.bibliotech.data.entity.Book;
import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.data.entity.BorrowingStatus;
import com.bibliotech.data.repository.BookRepository;
import com.bibliotech.data.repository.BorrowingRepository;
import com.bibliotech.service.interfaces.BorrowingEventService;
import com.bibliotech.service.interfaces.BorrowingService;
import com.bibliotech.web.dto.BorrowingRequestDTO;
import com.bibliotech.web.dto.BorrowingResponseDTO;
import com.bibliotech.web.exception.BusinessException;
import com.bibliotech.web.exception.ResourceNotFoundException;
import com.bibliotech.web.mapper.BorrowingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final BorrowingMapper borrowingMapper;
    private final BorrowingEventService borrowingEventService;

    public BorrowingServiceImpl(BorrowingRepository borrowingRepository,
                                BookRepository bookRepository,
                                BorrowingMapper borrowingMapper,
                                BorrowingEventService borrowingEventService) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.borrowingMapper = borrowingMapper;
        this.borrowingEventService = borrowingEventService;
    }

    @Override
    @Transactional
    public BorrowingResponseDTO processBorrowing(BorrowingRequestDTO requestDTO) {
        Book book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable: " + requestDTO.getBookId()));

        if (book.getStockDisponible() <= 0) {
            throw new BusinessException("Le livre n'est plus disponible en stock");
        }
        boolean alreadyBorrowed = borrowingRepository
                .existsByBook_IdAndStatus(requestDTO.getBookId(), BorrowingStatus.ONGOING);
        if (alreadyBorrowed) {
            throw new BusinessException("Ce livre est déjà emprunté");
        }
        long ongoingBorrowings = borrowingRepository.countByUserIdAndStatus(requestDTO.getUserId(), BorrowingStatus.ONGOING);
        if (ongoingBorrowings >= 3) {
            throw new BusinessException("L'utilisateur a deja 3 emprunts en cours");
        }

        book.setStockDisponible(book.getStockDisponible() - 1);
        bookRepository.save(book);

        Borrowing borrowing = Borrowing.builder()
                .book(book)
                .userId(requestDTO.getUserId())
                .borrowDate(LocalDate.now())
                .status(BorrowingStatus.ONGOING)
                .build();

        Borrowing saved = borrowingRepository.save(borrowing);
        borrowingEventService.publishEvent(saved, "CHECKOUT").subscribe();
        return borrowingMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BorrowingResponseDTO checkout(BorrowingRequestDTO requestDTO) {
        return processBorrowing(requestDTO);
    }

    @Override
    @Transactional
    public BorrowingResponseDTO returnBorrowing(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt introuvable: " + borrowingId));

        if (BorrowingStatus.RETURNED.equals(borrowing.getStatus())) {
            throw new BusinessException("Cet emprunt est deja rendu");
        }

        borrowing.setStatus(BorrowingStatus.RETURNED);
        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setStockDisponible(book.getStockDisponible() + 1);
        bookRepository.save(book);

        Borrowing saved = borrowingRepository.save(borrowing);
        borrowingEventService.publishEvent(saved, "RETURN").subscribe();
        return borrowingMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponseDTO> getActiveBorrowingsByUser(Long userId) {
        return borrowingRepository.findByUserIdAndStatus(userId, BorrowingStatus.ONGOING).stream()
                .map(borrowingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public int markOverdueBorrowings() {
        LocalDate limitDate = LocalDate.now().minusDays(14);
        List<Borrowing> overdueCandidates = borrowingRepository.findByStatusAndBorrowDateBefore(BorrowingStatus.ONGOING, limitDate);

        if (overdueCandidates.isEmpty()) {
            return 0;
        }

        overdueCandidates.forEach(b -> b.setStatus(BorrowingStatus.OVERDUE));
        borrowingRepository.saveAll(overdueCandidates);
        return overdueCandidates.size();
    }

}
