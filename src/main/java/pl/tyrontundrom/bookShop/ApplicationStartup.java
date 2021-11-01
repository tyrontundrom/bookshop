package pl.tyrontundrom.bookShop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogService catalogController;

    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle("Pan");
        books.forEach(System.out::println);
    }
}
