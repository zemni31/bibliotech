package com.bibliotech.web.controller;

import com.bibliotech.service.interfaces.BorrowingService;
import com.bibliotech.web.dto.BorrowingRequestDTO;
import com.bibliotech.web.dto.BorrowingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<BorrowingResponseDTO> checkout(@Valid @RequestBody BorrowingRequestDTO requestDTO) {
        return ResponseEntity.ok(borrowingService.checkout(requestDTO));
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BorrowingResponseDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowingService.returnBorrowing(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowingResponseDTO>> getActiveBorrowings(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowingService.getActiveBorrowingsByUser(userId));
    }
}
