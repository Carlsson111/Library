package se.lexicon.library.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.ALL})
    private Set<Book> writtenBooks = new HashSet<>();

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author(String firstName, String lastName, Set<Book> writtenBooks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.writtenBooks = writtenBooks;
    }

    public Author(int id, String firstName, String lastName, Set<Book> writtenBooks) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.writtenBooks = writtenBooks;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getWrittenBooks() {
        return writtenBooks;
    }

    public void setWrittenBooks(Set<Book> writtenBooks) {
        this.writtenBooks = writtenBooks;
    }

    public void addBook(Book book) {
        writtenBooks.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        writtenBooks.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id && Objects.equals(firstName, author.firstName) && Objects.equals(lastName, author.lastName) && Objects.equals(writtenBooks, author.writtenBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, writtenBooks);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", writtenBooks=" + writtenBooks +
                '}';
    }
}
