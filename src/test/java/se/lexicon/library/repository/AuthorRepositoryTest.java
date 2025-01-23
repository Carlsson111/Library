package se.lexicon.library.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.support.TransactionTemplate;
import se.lexicon.library.models.Author;
import se.lexicon.library.models.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void testFindByFirstName() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Smith");
        authorRepository.save(author);

        //act
        List<Author> authors = authorRepository.findByFirstName("John");

        //assert
        assertFalse(authors.isEmpty());
        authors.forEach(a -> System.out.println("Found Author by FirstName: " + a));

    }

    @Test
    void testFindByLastName() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Smith");
        authorRepository.save(author);

        //act
        List<Author> authors = authorRepository.findByLastName("Smith");

        //assert
        assertFalse(authors.isEmpty());
        authors.forEach(a -> System.out.println("Found Author by LastName: " + a));
    }

    @Test
    void testFindByFirstNameContainingOrLastNameContaining() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Smith");
        authorRepository.save(author);

        //act
        List<Author> authors = authorRepository.findByFirstNameContainingOrLastNameContaining("hn");

        //assert
        assertFalse(authors.isEmpty());
        authors.forEach(a -> System.out.println("Found Author by FirstNameContainingOrLastNameContaining: " + a));
    }

    @Test
    void testFindByWrittenBooks_BookId() {
        Book book = new Book();
        book.setIsbn("abc123");
        book.setTitle("Lord of the Rings");
        book.setMaxLoanDays(14);
        bookRepository.save(book);

        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Smith");
        author.getWrittenBooks().add(book);
        book.getAuthors().add(author); // Ensure the reverse relationship is set
        authorRepository.save(author);
        bookRepository.save(book); // Save the book again to ensure the relationship is persisted

        //act
        List<Author> authors = authorRepository.findByWrittenBooks_BookId(book.getId());

        //assert
        assertFalse(authors.isEmpty());
        authors.forEach(a -> System.out.println("Found Author by WrittenBooks_BookId: " + a));

    }

    @Test
    @Transactional
    void testUpdateAuthorNameByID() {
        Author author = new Author();
        author.setFirstName("OldFirstName");
        author.setLastName("OldLastName");
        authorRepository.save(author);

        //act
        authorRepository.updateAuthorNameByID(author.getId(), "NewFirstName", "NewLastName");
        entityManager.flush();
        entityManager.clear();
        Optional<Author> updatedAuthor = authorRepository.findById(author.getId());

        //assert
        assertTrue(updatedAuthor.isPresent());
        assertEquals("NewFirstName", updatedAuthor.get().getFirstName());
        assertEquals("NewLastName", updatedAuthor.get().getLastName());
        System.out.println("Updated Author: " + updatedAuthor.get());
    }

    @Test
    void testDeleteById() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Smith");
        authorRepository.save(author);

        //act
        authorRepository.deleteById(author.getId());
        Optional<Author> deletedAuthor = authorRepository.findById(author.getId());

        //assert
        assertFalse(deletedAuthor.isPresent());
        System.out.println("Deleted Author with ID: " + author.getId());


    }
}