package pl.tyrontundrom.bookShop.commons.catalog.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.tyrontundrom.bookShop.commons.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.commons.catalog.domain.Book;
import pl.tyrontundrom.bookShop.web.CreatedURI;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/catalog")
@RestController
@AllArgsConstructor
class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAll(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author) {
        if (title.isPresent() && author.isPresent()) {
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        } else if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        } else if (author.isPresent()) {
            return catalog.findByAuthor(author.get());
        } else {
            return catalog.findAll();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBook(@Valid @RequestBody RestBookCommand command) {
        Book book = catalog.addBook(command.toCreateCommand());
        return ResponseEntity.created(createdBookUri(book)).build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody RestBookCommand command) {
       CatalogUseCase.UpdateBookResponse response = catalog.updateBook(command.toUpdateCommand(id));
       if (!response.isSuccess()) {
           String message = String.join(",", response.getErrors());
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
       }
    }

    @PutMapping(value = "{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Got file: " + file.getOriginalFilename());
        catalog.updateBookCover(new CatalogUseCase.UpdateBookCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        ));
    }

    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        catalog.removeBookCover(id);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeById(id);
    }

    private URI createdBookUri(Book book) {
        return new CreatedURI("/" + book.getId().toString()).uri();
    }

    interface UpdateValidation {
    }

    interface CreateValidation {
    }


    @Data
    private static class RestBookCommand {

        @NotBlank(message = "provide a title", groups = {CreateValidation.class})
        private String title;

        @NotBlank(message = "provide an author", groups = {CreateValidation.class})
        private String author;

        @NotNull(message = "provide a year", groups = {CreateValidation.class})
        private Integer year;

        @NotNull(message = "provide a price", groups = {CreateValidation.class})
        @DecimalMin(value = "0.00", message = "provide a price", groups = {CreateValidation.class, UpdateValidation.class})
        private BigDecimal price;

        CatalogUseCase.CreateBookCommand toCreateCommand() {
            return new CatalogUseCase.CreateBookCommand(title, author, year, price);
        }

        CatalogUseCase.UpdateBookCommand toUpdateCommand(Long id) {
            return new CatalogUseCase.UpdateBookCommand(id, title, author, year, price);
        }
    }
}
