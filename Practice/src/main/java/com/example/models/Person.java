package com.example.models;


import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "people")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @Size(min = 2, max = 74, message = "FullName must be between 2 and 74 symbols")
    private String fullName;

    @Column(name = "email")
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @Column(name = "person_type")
    @Enumerated(EnumType.STRING)
    private PersonType type;

    @Transient
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirthTmp; // Hibernate won't see this field. it's just for custom date validation

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {}

    public Person(String fullName, String email, Date dateOfBirth, PersonType type) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PersonType getType() {
        return type;
    }

    public void setType(PersonType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Date getDateOfBirthTmp() {
        return dateOfBirthTmp;
    }

    public void setDateOfBirthTmp(Date dateOfBirthTmp) {
        this.dateOfBirthTmp = dateOfBirthTmp;
    }

    @Override
    public String toString() {
        return fullName + ", " + dateOfBirth + ", " + type;
    }
}
