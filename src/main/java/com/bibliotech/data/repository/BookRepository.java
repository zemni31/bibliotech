package com.bibliotech.data.repository;

import com.bibliotech.data.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Recherche par ISBN
    Optional<Book> findByIsbn(String isbn);

    // Recherche par titre (contient)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Livres disponibles en stock
    List<Book> findByStockDisponibleGreaterThan(Integer stock);

    Page<Book> findByStockDisponibleGreaterThan(Integer stock, Pageable pageable);
}
