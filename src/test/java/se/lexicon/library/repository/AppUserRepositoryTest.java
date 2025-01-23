package se.lexicon.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.library.models.AppUser;
import se.lexicon.library.models.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    DetailsRepository detailsRepository;

    @Test
    public void testFindById() {

        //details
        Details details = new Details();
        details.setName("Anders");
        details.setEmail("anders@example.com");
        details.setBirthDate(LocalDate.of(1998,1,1));
        detailsRepository.save(details);


        // user
        AppUser appUser = new AppUser();
        appUser.setUsername("AndersAndersson");
        appUser.setPassword("password");
        appUser.setRegDate(LocalDate.now());
        appUser.setUserDetails(details);
        appUserRepository.save(appUser);

        //act
        Optional<AppUser> foundUser = appUserRepository.findById(appUser.getId());
        //assert
        assertTrue(foundUser.isPresent());
        assertEquals(appUser.getUsername(), foundUser.get().getUsername());
        assertEquals(details.getId(), foundUser.get().getUserDetails().getId());


    }
    @Test
    public void testFindByUsername() {
        // user
        AppUser appUser = new AppUser();
        appUser.setUsername("AndersAndersson");
        appUser.setPassword("password");
        appUser.setRegDate(LocalDate.now());
        appUser.setUserDetails(null);
        appUserRepository.save(appUser);

        //Act
        Optional<AppUser> foundUser = appUserRepository.findByUsername("AndersAndersson");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(appUser.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testFindByRegDateBetween(){
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        AppUser appUser1 = new AppUser("User1", "password1", LocalDate.now(),null);
        AppUser appUser2 = new AppUser("User2", "password2", LocalDate.now(),null);

        appUserRepository.save(appUser1);
        appUserRepository.save(appUser2);

        //Act
        List<AppUser> users = appUserRepository.findByRegDateBetween(startDate, endDate);

        //Assert
        assertFalse(users.isEmpty());
        users.forEach(user -> System.out.println("Found User: " + user));
    }

    @Test
    public void testFindByUserDetailsId() {

        Details details = new Details();
        details.setName("Anders");
        details.setEmail("anders@example.com");
        details.setBirthDate(LocalDate.of(1998,1,1));
        detailsRepository.save(details);

        AppUser appUser = new AppUser();
        appUser.setUsername("AndersAndersson");
        appUser.setPassword("password");
        appUser.setRegDate(LocalDate.now());
        appUser.setUserDetails(details);
        appUserRepository.save(appUser);

        //act
        List<AppUser> users = appUserRepository.findByUserDetailsId(details.getId());

        //Assert
        assertFalse(users.isEmpty());
        users.forEach(user -> System.out.println("Found User: " + user));
        assertEquals(details.getId(), users.get(0).getUserDetails().getId());
    }
    @Test
    public void testFindByUserDetailsEmailIgnoreCase() {
        Details details = new Details();
        details.setName("Anders");
        details.setEmail("anders@example.com");
        details.setBirthDate(LocalDate.of(1998,1,1));
        detailsRepository.save(details);

        AppUser appUser = new AppUser();
        appUser.setUsername("AndersAndersson");
        appUser.setPassword("password");
        appUser.setRegDate(LocalDate.now());
        appUser.setUserDetails(details);
        appUserRepository.save(appUser);

        //acr
        Optional<AppUser> foundUser = appUserRepository.findByUserDetailsEmailIgnoreCase("ANDERS@example.com");

        assertTrue(foundUser.isPresent());
        System.out.println("Found User: " + foundUser.get());
        assertEquals(details.getEmail(), foundUser.get().getUserDetails().getEmail());


    }


}
