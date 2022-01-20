package pl.tyrontundrom.bookShop.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tyrontundrom.bookShop.catalog.application.port.AuthorsUseCase;
import pl.tyrontundrom.bookShop.catalog.db.AuthorJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Author;

import java.util.List;

@Service
@AllArgsConstructor
class AuthorsService implements AuthorsUseCase {
    private final AuthorJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }
}
