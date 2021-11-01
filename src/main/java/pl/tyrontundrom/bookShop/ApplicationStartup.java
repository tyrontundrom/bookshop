package pl.tyrontundrom.bookShop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogService;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogService catalogController;
    private final String title;
    private final Long limit;

    public ApplicationStartup(CatalogService catalogController,
                              @Value("${bookShop.catalog.query}") String title,
                              @Value("${bookShop.catalog.limit:2}") Long limit) {
        this.catalogController = catalogController;
        this.title = title;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle(title);
        books.stream().limit(limit).forEach(System.out::println);
    }
}
