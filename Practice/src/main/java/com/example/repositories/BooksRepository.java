package com.example.repositories;

import com.example.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameStartingWith(String name);

    @Query(value = "select b from Book b where b.owner.id = :id")
    List<Book> getPersonBooks(@Param("id") int id);
}