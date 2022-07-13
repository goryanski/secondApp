package com.example.util;

import com.example.dao.BookDAO;
import com.example.dao.PersonDAO;
import com.example.models.Book;
import com.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;


@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;

    @Autowired
    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        // book writing year cannot be greater than current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(book.getYear() > currentYear) {
            errors.rejectValue("year", "", "Book writing year cannot be greater than the current year");
        }
    }
}

