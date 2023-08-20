package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.BookSearchParameters;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books management",description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description =
            "Implemented pagination,can get list of books by page and size of page"
                    + " for getting books, also sort by fields")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by its id",description = "get book by its id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create a new book",description = "Create a new book and add to database")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by its id",description = "Update a book by its id")
    public BookDto updateBookById(@RequestBody @Valid CreateBookRequestDto requestDto,
                                  @PathVariable Long id) {
        return bookService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by id",description = "Delete a book by id")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "search by criteria title and/or author",
            description = "search by criteria title and/or author")
    public List<BookDto> search(BookSearchParameters parameters) {
        return bookService.search(parameters);
    }

}
