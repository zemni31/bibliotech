package com.bibliotech.service.impl;

import com.bibliotech.data.entity.Book;
import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.data.entity.BorrowingStatus;
import com.bibliotech.data.repository.BookRepository;
import com.bibliotech.data.repository.BorrowingRepository;
import com.bibliotech.service.interfaces.BorrowingEventService;
import com.bibliotech.web.dto.BorrowingRequestDTO;
import com.bibliotech.web.dto.BorrowingResponseDTO;
import com.bibliotech.web.exception.BusinessException;
import com.bibliotech.web.mapper.BorrowingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceImplTest {

    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingMapper borrowingMapper;
    @Mock
    private BorrowingEventService borrowingEventService;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Test
    void processBorrowing_shouldSucceed_whenBookInStock() {
        BorrowingRequestDTO request = BorrowingRequestDTO.builder()
                .bookId(1L)
                .userId(10L)
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .stockDisponible(2)
                .build();

        Borrowing savedBorrowing = Borrowing.builder()
                .id(100L)
                .book(book)
                .userId(10L)
                .borrowDate(LocalDate.now())
                .status(BorrowingStatus.ONGOING)
                .build();

        BorrowingResponseDTO expected = BorrowingResponseDTO.builder()
                .id(100L)
                .bookId(1L)
                .userId(10L)
                .status(BorrowingStatus.ONGOING)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRepository.countByUserIdAndStatus(10L, BorrowingStatus.ONGOING)).thenReturn(0L);
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(savedBorrowing);
        when(borrowingMapper.toDto(savedBorrowing)).thenReturn(expected);
        when(borrowingEventService.publishEvent(any(Borrowing.class), eq("CHECKOUT"))).thenReturn(Mono.empty());

        BorrowingResponseDTO result = borrowingService.processBorrowing(request);

        assertEquals(100L, result.getId());
        assertEquals(1, book.getStockDisponible());
        verify(bookRepository).save(book);
        verify(borrowingRepository).save(any(Borrowing.class));
    }

    @Test
    void processBorrowing_shouldThrowBusinessException_whenStockEmpty() {
        BorrowingRequestDTO request = BorrowingRequestDTO.builder()
                .bookId(1L)
                .userId(10L)
                .build();

        Book book = Book.builder()
                .id(1L)
                .stockDisponible(0)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BusinessException.class, () -> borrowingService.processBorrowing(request));
    }
}
