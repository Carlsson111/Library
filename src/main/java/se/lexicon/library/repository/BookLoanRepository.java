package se.lexicon.library.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.lexicon.library.models.BookLoan;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {

    List<BookLoan> findByBorrowerId(int borrowerId);

    List<BookLoan>findByBookId(int bookId);

    List<BookLoan> findByReturnedFalse();

    List<BookLoan>findByDueDateBeforeAndReturnedFalse(LocalDate dueDate);

    List<BookLoan> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);

    // default void markAsReturned(int loanId){
        //BookLoan bookLoan = findById(loanId).orElseThrow(() -> new IllegalArgumentException("BookLoan not found"));
        //bookLoan.setReturned(true);
        //save(bookLoan);

    @Modifying
    @Transactional
    @Query("UPDATE BookLoan b SET b.returned = true WHERE b.id = :loanId")
    void markAsReturned(@Param("loanId") int loanId);






}
