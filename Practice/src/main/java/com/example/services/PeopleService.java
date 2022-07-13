package com.example.services;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import com.example.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository, BooksRepository booksRepository1) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository1;
    }

    public List<Person> getAll() {
       return peopleRepository.findAll();
    }

    public Person get(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public Optional<Person> getPersonByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public List<Book> getPersonBooks(int id) {
        List<Book> books = booksRepository.getPersonBooks(id);
        if(books.size() > 0) {
            // check every book and find out if there are expired books
            books.forEach(book -> {
                // book is expired if person took it more than 10 days ago
                long diffInMilliseconds = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                int tenDaysInMilliseconds = 864000000;

                if (diffInMilliseconds > tenDaysInMilliseconds) {
                    book.setExpired(true);
                }
            });
            return books;
        } else {
            return Collections.emptyList();
        }
    }
}
