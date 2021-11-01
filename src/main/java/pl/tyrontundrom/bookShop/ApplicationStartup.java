package pl.tyrontundrom.bookShop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogService;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogService catalogController;

    public ApplicationStartup(CatalogService catalogService) {
        this.catalogController = catalogService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Book> books = catalogController.findByTitle("Pan");
        books.forEach(System.out::println);
    }
}
