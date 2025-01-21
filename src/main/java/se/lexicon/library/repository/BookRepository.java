package se.lexicon.library.repository;

import org.springframework.data.repository.CrudRepository;
import se.lexicon.library.models.Book;


import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByIsbnIgnoreCase(String isbn);

    List<Book> findByTitleContaining(String title);

    List<Book> findByMaxLoanDaysLessThan(int maxLoanDays);
}
