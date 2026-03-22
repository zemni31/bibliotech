package com.bibliotech.service.impl;

import com.bibliotech.data.entity.Author;
import com.bibliotech.data.entity.Book;
import com.bibliotech.data.entity.Category;
import com.bibliotech.data.repository.AuthorRepository;
import com.bibliotech.data.repository.BookRepository;
import com.bibliotech.data.repository.CategoryRepository;
import com.bibliotech.service.interfaces.BookService;
import com.bibliotech.web.dto.BookRequestDTO;
import com.bibliotech.web.dto.BookResponseDTO;
import com.bibliotech.web.exception.BusinessException;
import com.bibliotech.web.exception.ResourceNotFoundException;
import com.bibliotech.web.mapper.BookMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> getAvailableBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByStockDisponibleGreaterThan(0, pageable).map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO requestDTO) {
        if (bookRepository.findByIsbn(requestDTO.getIsbn()).isPresent()) {
            throw new BusinessException("ISBN deja existant: " + requestDTO.getIsbn());
        }

        Book book = bookMapper.toEntity(requestDTO);
        Set<Category> categories = resolveCategories(requestDTO.getCategoryIds());
        book.setCategories(categories);

        Book savedBook = bookRepository.save(book);

        if (requestDTO.getAuthorId() != null) {
            Author author = authorRepository.findById(requestDTO.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable: " + requestDTO.getAuthorId()));
            author.setBook(savedBook);
            authorRepository.save(author);
            savedBook.getAuthors().add(author);
        }

        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable: " + id));
        bookRepository.delete(book);
    }

    private Set<Category> resolveCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new ResourceNotFoundException("Une ou plusieurs categories sont introuvables");
        }
        return new HashSet<>(categories);
    }
}
