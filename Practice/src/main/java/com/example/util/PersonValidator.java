package com.example.util;

import com.example.dao.PersonDAO;
import com.example.models.Person;
import com.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;
    private String validationMode;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if(validationMode.equals("add")) {
            // full name and email must be unique, but only if we create a new person, otherwise we won't update existent person correctly (can't leave fields fullName and email without changes)
            if (peopleService.getPersonByFullName(person.getFullName()).isPresent()) {
                errors.rejectValue("fullName", "", "Person with this full name already exists");
            }
            if (peopleService.getPersonByEmail(person.getEmail()).isPresent()) {
                errors.rejectValue("email", "", "Person with this email already exists");
            }
        }



        // dateOfBirth validation
        // if date from user input is not valid - in field person.getDateOfBirth() will be null
        Date date = person.getDateOfBirthTmp();
        if(date != null) {
            // check date > 1900 && date < now
            Date minDate = new GregorianCalendar(1900, Calendar.JANUARY , 1).getTime();
            Date maxDate = Calendar.getInstance().getTime(); // current

            if(date.before(minDate) || date.after(maxDate)) {
                errors.rejectValue("dateOfBirth", "", "Date of birth cannot be earlier then 1900 and  greater than the current date");
            }
            else {
                // if there are no errors - set correct date to field which linked to Hibernate
                person.setDateOfBirth(date);
                // field dateOfBirthTmp we need to avoid system error which occur if user writes invalid date (like "sdsdvsdvg"). this error writes to errors.rejectValue(), but we don't need such error in our errors list, so this error remains for field dateOfBirthTmp, instead of field dateOfBirth, where we put our errors messages without system's.
            }
        } else {
            errors.rejectValue("dateOfBirth", "", "Date of birth can be only in format (dd/mm/yyyy)");
        }
    }


    public String getValidationMode() {
        return validationMode;
    }

    public void setValidationMode(String validationMode) {
        this.validationMode = validationMode;
    }
}
