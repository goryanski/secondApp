package com.example.services;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAll(boolean sortByYear) {
        if (sortByYear){
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> getWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book get(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book bookNewVersion) {
        Book bookOldVersion = booksRepository.findById(id).get();
        bookNewVersion.setId(id);
        // to save link between book and owner we need to take owner from bookOldVersion and set it to bookNewVersion
        // because bookNewVersion entity comes to us from the form, and it has field owner = null
        bookNewVersion.setOwner(bookOldVersion.getOwner());

        booksRepository.save(bookNewVersion);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> searchByName(String partOfName) {
        return booksRepository.findByNameStartingWith(partOfName);
    }

    public Person getBookOwner(int id) {
        // find book by id, then using map select only field "owner" from the book entity or return null if book has no owner
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }


    // release book (remove link to person in table) - this method is called when a person returns a book to the library
    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    // call book setters in a lambda
                    book.setOwner(null);
                    book.setTakenAt(null);
                    booksRepository.save(book);
                });
    }

    // assign book to the selected person (create link to person in table) - this method is called when a person takes a book from the library
    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    // set current time in a milliseconds to check later if this book is not expired
                    book.setTakenAt(new Date());
                    booksRepository.save(book);
                }
        );
    }
}