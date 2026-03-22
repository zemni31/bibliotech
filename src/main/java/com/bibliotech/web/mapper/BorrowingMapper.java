package com.bibliotech.web.mapper;

import com.bibliotech.data.entity.Borrowing;
import com.bibliotech.web.dto.BorrowingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    BorrowingResponseDTO toDto(Borrowing borrowing);
}
