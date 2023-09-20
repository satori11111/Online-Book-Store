package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.mapper.CategoryMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.service.CategoryService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find category by id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Set<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CategoryDto create(CreateCategoryRequestDto requestDto) {
        Category model = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public CategoryDto updateById(CreateCategoryRequestDto requestDto, Long id) {
        validateCategoryExists(id);
        Category model = categoryMapper.toModel(requestDto);
        model.setId(id);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public void deleteById(Long id) {
        validateCategoryExists(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        List<Book> books = categoryRepository.getBooksByCategoriesId(id);
        return books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();

    }

    private void validateCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find category by id: " + id);
        }
    }
}
