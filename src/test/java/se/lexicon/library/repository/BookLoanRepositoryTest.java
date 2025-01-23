package se.lexicon.library.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.library.models.AppUser;
import se.lexicon.library.models.Book;
import se.lexicon.library.models.BookLoan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BookLoanRepositoryTest {
    @Autowired
    BookLoanRepository bookLoanRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    EntityManager entityManager;
    @Test
    void testFindByBorrowerId() {
        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book = new Book("12365","EldFlugan",14);
        bookRepository.save(book);

        BookLoan loan1 = new BookLoan(LocalDate.now().minusDays(2), LocalDate.now().plusDays(14), false, borrower, book);
        BookLoan loan2 = new BookLoan(LocalDate.now().minusDays(3), LocalDate.now().plusDays(13), false, borrower, book);

        bookLoanRepository.save(loan1);
        bookLoanRepository.save(loan2);

        //act
        List<BookLoan> loans = bookLoanRepository.findByBorrowerId(borrower.getId());

        //assert
        assertFalse(loans.isEmpty());
        loans.forEach(loan -> System.out.println("Found Loan by BorrowerId: " + loan));
    }

    @Test
    void testFindByBookId() {
        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book = new Book("12365","EldFlugan",14);
        bookRepository.save(book);

        BookLoan bookLoan = new BookLoan(LocalDate.now().minusDays(2), LocalDate.now().plusDays(14), false, borrower, book);
        bookLoanRepository.save(bookLoan);

        //act
        List<BookLoan> loans = bookLoanRepository.findByBookId(book.getId());

        //assert
        assertFalse(loans.isEmpty());
        loans.forEach(loan -> System.out.println("Found Loan by BookId: " + loan));    }

    @Test
    void testFindByReturnedFalse() {
        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book = new Book("12365","EldFlugan",14);
        bookRepository.save(book);

        BookLoan bookLoan = new BookLoan(LocalDate.now().minusDays(1), LocalDate.now().plusDays(15), false, borrower, book);
        bookLoanRepository.save(bookLoan);

        //act
        List<BookLoan> loans = bookLoanRepository.findByReturnedFalse();

        //assert
        assertFalse(loans.isEmpty());
        loans.forEach(loan -> System.out.println("Found Loan by ReturnedFalse: " + loan));

    }

    @Test
    void testFindByDueDateBeforeAndReturnedFalse() {
        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book = new Book("12365","EldFlugan",14);
        bookRepository.save(book);

        BookLoan bookLoan = new BookLoan(LocalDate.now().minusWeeks(2), LocalDate.now().minusDays(1), false, borrower, book);
        bookLoanRepository.save(bookLoan);

        //act
        List<BookLoan> loans = bookLoanRepository.findByDueDateBeforeAndReturnedFalse(LocalDate.now());

        //assert
        assertFalse(loans.isEmpty());
        loans.forEach(loan -> System.out.println("Found Loan by DueDateBeforeAndReturnedFalse: " + loan));

    }

    @Test
    void testFindByLoanDateBetween() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);

        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book1 = new Book("12365","EldFlugan",14);
        Book book2 = new Book("1265","Lord of the Rings",14);
        bookRepository.save(book1);
        bookRepository.save(book2);

        BookLoan loan1 = new BookLoan(LocalDate.now().minusDays(2), LocalDate.now().plusDays(14), false, borrower, book1);
        BookLoan loan2 = new BookLoan(LocalDate.now().minusDays(4), LocalDate.now().plusDays(12), false, borrower, book2);
        bookLoanRepository.save(loan1);
        bookLoanRepository.save(loan2);

        //act
        List<BookLoan> loans = bookLoanRepository.findByLoanDateBetween(startDate, endDate);

        //assert
        assertFalse(loans.isEmpty());
        loans.forEach(loan -> System.out.println("Found Loan by LoanDateBetween: " + loan));

    }

    @Test
    @Transactional
    void testMarkAsReturned() {
        AppUser borrower = new AppUser("Borrower", "password", LocalDate.now(), null);
        appUserRepository.save(borrower);

        Book book = new Book("1265","Lord of the Rings",14);
        bookRepository.save(book);

        BookLoan loan = new BookLoan(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), false, borrower, book);
        bookLoanRepository.save(loan);

        //act
        bookLoanRepository.markAsReturned(loan.getId());
        // flush
        entityManager.flush();
        entityManager.clear();

        Optional<BookLoan> updatedLoan = bookLoanRepository.findById(loan.getId());

        //assert
        assertTrue(updatedLoan.isPresent());
        assertTrue(updatedLoan.get().isReturned());
        System.out.println("Marked as Returned: " + updatedLoan.get());
    }
}