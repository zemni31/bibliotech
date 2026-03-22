package com.bibliotech.service.interfaces;

import com.bibliotech.web.dto.BookRequestDTO;
import com.bibliotech.web.dto.BookResponseDTO;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<BookResponseDTO> getAvailableBooks(int page, int size);
    BookResponseDTO createBook(BookRequestDTO requestDTO);
    BookResponseDTO getBookById(Long id);
    void deleteBook(Long id);
}
