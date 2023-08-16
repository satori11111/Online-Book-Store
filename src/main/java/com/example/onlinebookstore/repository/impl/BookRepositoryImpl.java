package com.example.onlinebookstore.repository.impl;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.BookRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private SessionFactory sessionFactory;

    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session currentSession = null;
        Transaction transaction = null;
        try {
            currentSession = sessionFactory.openSession();
            transaction = currentSession.beginTransaction();
            currentSession.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add book: " + book, e);
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session currentSession = sessionFactory.openSession();) {
            Query<Book> getAllCinemaHallsQuery =
                    currentSession.createQuery("FROM Book", Book.class);
            return getAllCinemaHallsQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't find all books", e);
        }
    }
}
