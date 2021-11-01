package pl.tyrontundrom.bookShop.catalog.domain;

import java.util.List;

public interface CatalogRepository {
    List<Book> findAll();
}
