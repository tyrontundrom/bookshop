package pl.tyrontundrom.bookShop.catalog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BestsellerCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepository() {
        storage.put(1L, new Book(1L, "Kolejne 365 dni", "Blanka Lipińska", 2019));
        storage.put(2L, new Book(2L, "Nasz ostatni dzień", "Adam Silvera", 2017));
        storage.put(3L, new Book(3L, "Obcy", "Albert Camus", 1957));
        storage.put(4L, new Book(4L, "Mały Książę", "Antoine de Saint-Exupéry", 1943));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
