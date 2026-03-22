package com.bibliotech.web.mapper;

import com.bibliotech.data.entity.Author;
import com.bibliotech.data.entity.Book;
import com.bibliotech.data.entity.Category;
import com.bibliotech.web.dto.BookRequestDTO;
import com.bibliotech.web.dto.BookResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    Book toEntity(BookRequestDTO dto);

    @Mapping(target = "authorName", expression = "java(getFirstAuthorName(book.getAuthors()))")
    @Mapping(target = "categories", expression = "java(toCategoryLabels(book.getCategories()))")
    BookResponseDTO toDto(Book book);

    default String getFirstAuthorName(Set<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return null;
        }
        return authors.stream()
                .map(Author::getName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    default List<String> toCategoryLabels(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream()
                .map(Category::getLabel)
                .filter(Objects::nonNull)
                .toList();
    }
}
