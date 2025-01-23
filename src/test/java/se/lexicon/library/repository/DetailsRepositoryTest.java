package se.lexicon.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.library.models.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DetailsRepositoryTest {

    @Autowired
    DetailsRepository detailsRepository;

    @Test
    public void testFindByEmail() {
        Details details = new Details();
        details.setName("Anders");
        details.setEmail("anders@example.com");
        details.setBirthDate(LocalDate.of(1998,1,1));
        detailsRepository.save(details);

        //act
        Optional<Details> foundDetails = detailsRepository.findByEmail("anders@example.com");

        //assert
        assertTrue(foundDetails.isPresent());
        System.out.println("Found Details by Email: " + foundDetails.get());
        assertEquals("anders@example.com", foundDetails.get().getEmail());
    }

    @Test
    public void testFindByNameIgnoreCase() {
        Details details1 = new Details("test@test.com", "Bjorn ", LocalDate.of(1998,1,1));
        Details details2 = new Details("test1@test.com", "Anders", LocalDate.of(1998,1,1));
        detailsRepository.save(details1);
        detailsRepository.save(details2);

        //act
        List<Details> foundDetails = detailsRepository.findByNameIgnoreCase("ANDERS");

        //
        assertFalse(foundDetails.isEmpty());
        foundDetails.forEach(details -> System.out.println("Found Details by Name Ignore Case: " + details));

    }


}
