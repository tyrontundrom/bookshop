package pl.tyrontundrom.bookShop.catalog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalog;

    @Autowired
    CatalogController controller;

    @Test
    void shouldGetAllBooks() {
        // given
        Book effective = new Book("Effective Java", 2005, new BigDecimal("99.00"), 50L);
        Book concurrency = new Book("Concurrency Java in Practice", 2006, new BigDecimal("129.00"), 50L);
        Mockito.when(catalog.findAll()).thenReturn(List.of(effective, concurrency));
        // when
        List<Book> all = controller.getAll(Optional.empty(), Optional.empty());

        //then
        assertEquals(2, all.size());
    }

}