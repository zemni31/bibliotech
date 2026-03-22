package com.bibliotech.service.interfaces;

import com.bibliotech.web.dto.BorrowingRequestDTO;
import com.bibliotech.web.dto.BorrowingResponseDTO;
import java.util.List;

public interface BorrowingService {
    BorrowingResponseDTO processBorrowing(BorrowingRequestDTO requestDTO);
    BorrowingResponseDTO checkout(BorrowingRequestDTO requestDTO);
    BorrowingResponseDTO returnBorrowing(Long borrowingId);
    List<BorrowingResponseDTO> getActiveBorrowingsByUser(Long userId);
    int markOverdueBorrowings();
}
