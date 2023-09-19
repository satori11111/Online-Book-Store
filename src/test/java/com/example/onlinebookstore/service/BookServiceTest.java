package com.example.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookSearchParametersDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.book.BookRepository;
import com.example.onlinebookstore.repository.specification.SpecificationBuilder;
import com.example.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static Book book;
    private static CreateBookRequestDto createBookRequest;
    private static BookSearchParametersDto searchParameters;
    private static BookDto bookDto;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private SpecificationBuilder<Book> specificationBuilder;

    @BeforeAll
    static void beforeAll() {
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Example Book");
        bookDto.setAuthor("John Doe");
        bookDto.setIsbn("978-1234567890");
        bookDto.setPrice(new BigDecimal("19.99"));
        bookDto.setDescription("An example book description.");
        bookDto.setCoverImage("book_cover.jpg");
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(101L);
        categoryIds.add(102L);
        bookDto.setCategoriesIds(categoryIds);

        book = new Book();
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(new BigDecimal("19.99"));
        book.setDescription("A classic novel about the Jazz Age");
        book.setCoverImage("great_gatsby.jpg");
        Set<Category> categories = new HashSet<>();
        categories.add(new Category());
        book.setCategories(categories);

        createBookRequest = new CreateBookRequestDto();
        createBookRequest.setTitle("Pride and Prejudice");
        createBookRequest.setAuthor("Jane Austen");
        createBookRequest.setIsbn("978-0-14-143951-8");
        createBookRequest.setPrice(new BigDecimal("12.99"));
        createBookRequest.setDescription("A classic romance novel");
        createBookRequest.setCoverImage("pride_prejudice.jpg");
        Set<Long> categoryIdsForDto = new HashSet<>();
        categoryIdsForDto.add(1L);
        categoryIdsForDto.add(2L);
        createBookRequest.setCategoryIds(categoryIdsForDto);

        String[] authors = {"Author 1", "Author 2"};
        String[] titles = {"Title 1", "Title 2"};
        searchParameters = new BookSearchParametersDto(authors, titles);
    }

    @Test
    @DisplayName("Test getById with valid request")
    public void getById_validId_returnsBook() {
        when(bookRepository.findByIdJoinCategories(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        assertEquals(bookDto, bookService.getBookById(1L));
        verify(bookMapper).toDto(any(Book.class));
        verify(bookRepository).findByIdJoinCategories(anyLong());
    }

    @Test
    @DisplayName("Test getBookById with  non valid request")
    public void getById_nonValidId_throwsException() {
        when(bookRepository.findByIdJoinCategories(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(1L));
        String expectedMessage = "Can't find book with id: 1";
        assertEquals(expectedMessage, actual.getMessage());

        verify(bookRepository).findByIdJoinCategories(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Test create with valid request")
    public void createBook_validBook_returnsCreatedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        assertEquals(bookDto, bookService.save(createBookRequest));
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toModel(any(CreateBookRequestDto.class));
        verify(bookMapper).toDto(any(Book.class));
    }

    @Test
    @DisplayName("Test findAllBooks with valid request")
    public void findAllBooks_validRequest_returnsList() {
        Pageable pageable = Pageable.ofSize(5);
        List<BookDto> expectedCategoryDtos = List.of(bookDto);
        List<Book> books = List.of(book);

        when(bookRepository.findAllJoinCategories(pageable)).thenReturn(books);
        when(bookMapper.toDto(any(Book.class)))
                .thenReturn(expectedCategoryDtos.iterator().next());
        List<BookDto> actual = bookService.findAll(pageable);

        verify(bookRepository).findAllJoinCategories(pageable);
        verify(bookMapper, times(books.size())).toDto(any(Book.class));
        assertEquals(expectedCategoryDtos, actual);
    }

    @Test
    @DisplayName("Test update with valid request")
    public void update_validBook_returnsUpdatedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        assertEquals(bookDto, bookService.update(createBookRequest, 1L));
        verify(bookRepository).save(book);
        verify(bookMapper).toModel(any(CreateBookRequestDto.class));
        verify(bookMapper).toDto(any(Book.class));
        verify(bookRepository).existsById(anyLong());
    }

    @Test
    @DisplayName("Test update with non valid request")
    public void updateBook_nonValidId_throwsException() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(createBookRequest, 1L));
        assertEquals("Can't find book with id: 1", actual.getMessage());
        verify(bookRepository).existsById(anyLong());
    }

    @Test
    @DisplayName("Test deleteById with non valid request")
    public void deleteById_nonValidId_throwsException_notOk() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(1L));
        assertEquals("Can't find book with id: 1", actual.getMessage());
        verify(bookRepository).existsById(anyLong());
    }

    @Test
    @DisplayName("Test search with valid request")
    public void searchBooks_validSearchParams_returnsMatchingBooks() {
        Specification<Book> specification = mock(Specification.class);
        when(specificationBuilder.build(any(BookSearchParametersDto.class)))
                .thenReturn(specification);
        when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        assertEquals(List.of(bookDto), bookService.search(searchParameters));
        verify(bookRepository).findAll(any(Specification.class));
        verify(specificationBuilder).build(any(BookSearchParametersDto.class));
    }
}
