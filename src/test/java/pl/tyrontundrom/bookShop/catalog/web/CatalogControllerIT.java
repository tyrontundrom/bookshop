package pl.tyrontundrom.bookShop.catalog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.tyrontundrom.bookShop.catalog.db.AuthorJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Author;
import pl.tyrontundrom.bookShop.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CatalogControllerIT {

    @Autowired
    AuthorJpaRepository authorJpaRepository;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    CatalogController controller;

    @Test
    void getAllBooks() {
        // given
        givenEffectiveJava();
        givenJavaConcurrencyInPractice();
        // when
        List<Book> all = controller.getAll(Optional.empty(), Optional.empty());

        // then
        assertEquals(2, all.size());
    }



    @Test
    void getBooksByAuthor() {
        // given
        givenEffectiveJava();
        givenJavaConcurrencyInPractice();
        // when
        List<Book> all = controller.getAll(Optional.empty(), Optional.of("Bloch"));

        // then
        assertEquals(1, all.size());
        assertEquals("Effective Java", all.get(0).getTitle());
    }

    @Test
    void getBooksByTitle() {
        // given
        givenEffectiveJava();
        givenJavaConcurrencyInPractice();

        // when
        List<Book> all = controller.getAll(Optional.of("Java Concurrency in Practice"), Optional.empty());

        // then
        assertEquals(1 , all.size());
        assertEquals("Java Concurrency in Practice", all.get(0).getTitle());
    }

    private void givenJavaConcurrencyInPractice() {
        Author goetz = authorJpaRepository.save(new Author("Brian Goetz"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Concurrency in Practice",
                Set.of(goetz.getId()),
                2007,
                new BigDecimal("129.90"),
                50L
        ));
    }

    private void givenEffectiveJava() {
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2006,
                new BigDecimal("99.90"),
                50L
        ));
    }

}