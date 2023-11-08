package com.example.springlibraryapi.controller;

import com.example.springlibraryapi.entity.Book;
import com.example.springlibraryapi.exception.ResourceNotFoundException;
import com.example.springlibraryapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(books,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        if (!bookService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("There isn't book with id : " + id);
        }
        Book book = bookService.findById(id).get();
        return new ResponseEntity<>(book,HttpStatus.FOUND);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getByIsbn(@PathVariable String isbn) {
        if (!bookService.findByIsbn(isbn).isPresent()) {
            throw new ResourceNotFoundException("There isn't book with ISBN: " + isbn);
        }
        Book book = bookService.findByIsbn(isbn).get();
        return new ResponseEntity<>(book, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Book> add(@RequestBody Book book) {
        bookService.store(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@RequestBody Book book,
                                       @PathVariable Long id) {
        return bookService.findById(id).map(updatedBook -> {
            updatedBook.setISBN(book.getISBN());
            updatedBook.setName(book.getName());
            updatedBook.setGenre(book.getGenre());
            updatedBook.setDescription(book.getDescription());
            updatedBook.setAuthor(book.getAuthor());
            bookService.store(updatedBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }).orElseGet(() -> {
            Book newBook = bookService.store(book);
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> delete(@PathVariable("id") Long id) {
        Optional<Book> bookToDelete = bookService.findById(id);
        if (!bookToDelete.isPresent())
            throw new ResourceNotFoundException("There isn't book with id : " + id);
        bookService.delete(id);
        return new ResponseEntity<>(bookToDelete.get(), HttpStatus.NO_CONTENT);
    }


}
