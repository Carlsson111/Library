package se.lexicon.library.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.lexicon.library.models.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author,Integer> {
    List<Author> findByFirstName(String firstName);

    List<Author> findByLastName(String lastName);

    @Query("SELECT a FROM Author a WHERE a.firstName like %:keyword% OR a.lastName LIKE %:keyword%")
    List<Author> findByFirstNameContainingOrLastNameContaining(@Param("keyword") String keyword);


    @Query("select a from Author a join a.writtenBooks b where b.id = :bookId")
    List<Author> findByWrittenBooks_BookId(int bookId);

    @Modifying
    @Transactional
    @Query("UPDATE Author a set a.firstName = :firstName, a.lastName = :lastName WHERE a.id = :id")
    void updateAuthorNameByID(@Param("id")int id, @Param("firstName")String firstName, @Param("lastName")String lastName);

    void deleteById(int id);

}
