package pl.tyrontundrom.bookShop.catalog.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tyrontundrom.bookShop.catalog.application.port.AuthorsUseCase;
import pl.tyrontundrom.bookShop.catalog.domain.Author;

import java.util.List;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
class AuthorsController {
    private final AuthorsUseCase authors;

    @GetMapping
    public List<Author> findAll() {
        return authors.findAll();
    }
}
