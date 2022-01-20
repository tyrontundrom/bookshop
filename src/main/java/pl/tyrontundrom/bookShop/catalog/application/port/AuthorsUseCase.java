package pl.tyrontundrom.bookShop.catalog.application.port;

import pl.tyrontundrom.bookShop.catalog.domain.Author;

import java.util.List;

public interface AuthorsUseCase {
    List<Author> findAll();
}
