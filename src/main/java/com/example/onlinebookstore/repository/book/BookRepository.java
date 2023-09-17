package com.example.onlinebookstore.repository.book;

import com.example.onlinebookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {
    @Query("FROM Book b JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findByIdBookAndItsCategories(Long id);

    @Query("FROM Book b JOIN FETCH b.categories")
    List<Book> findAllBooksAndTheirCategories(Pageable pageable);

    Book getBookById(Long id);
}
