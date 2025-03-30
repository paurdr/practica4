package com.example.booksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.booksearch.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
