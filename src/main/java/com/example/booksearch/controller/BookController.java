package com.example.booksearch.controller;

import com.example.booksearch.model.Book;
import com.example.booksearch.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public BookController(BookRepository bookRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
    }

    // 1. Crear un nuevo libro
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // 2. Leer un libro por id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Leer todos los libros
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 4. Actualizar un libro existente
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setIsbn(updatedBook.getIsbn());
                    Book savedBook = bookRepository.save(book);
                    return ResponseEntity.ok(savedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Borrar un libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar libros en OpenLibrary por título
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String title) {
        String url = "https://openlibrary.org/search.json?title=" + title;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}
