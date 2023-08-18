package com.example.onlinebookstore;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("rowling");
            book.setIsbn("123455");
            book.setTitle("Harry Potter");
            book.setPrice(BigDecimal.ONE);
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
