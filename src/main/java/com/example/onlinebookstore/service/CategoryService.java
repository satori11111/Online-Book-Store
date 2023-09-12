package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto getCategoryById(Long id);

    Set<CategoryDto> findAll(Pageable pageable);

    CategoryDto create(CreateCategoryRequestDto requestDto);

    CategoryDto updateById(CreateCategoryRequestDto requestDto, Long id);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
