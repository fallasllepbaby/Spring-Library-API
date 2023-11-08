package com.example.springlibraryapi.service;

import com.example.springlibraryapi.entity.Book;
import com.example.springlibraryapi.exception.AlreadyExistException;
import com.example.springlibraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByISBN(isbn);
    }

    public Book store(Book book) {
        if (bookRepository.findByISBN(book.getISBN()).isPresent()) {
            throw new AlreadyExistException("Book with ISBN: " + book.getISBN() + "already exists");
        }
        return bookRepository.save(book);
    }
}
