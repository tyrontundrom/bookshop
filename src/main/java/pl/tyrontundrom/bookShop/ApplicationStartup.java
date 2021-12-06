package pl.tyrontundrom.bookShop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.port.PlaceOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.Recepient;


import java.math.BigDecimal;
import java.util.List;

import static pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase.*;
import static pl.tyrontundrom.bookShop.order.application.port.PlaceOrderUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final String author;
    private final Long limit;

    public ApplicationStartup(CatalogUseCase catalog,
                              PlaceOrderUseCase placeOrder,
                              QueryOrderUseCase queryOrder,
                              @Value("${bookShop.catalog.query}") String title,
                              @Value("${bookShop.catalog.author}")String author,
                              @Value("${bookShop.catalog.limit:2}") Long limit) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
        this.author = author;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book panTadeusz = catalog.findOneByTitle("Pan Tadeusz").orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book chlopi = catalog.findOneByTitle("Chłopi").orElseThrow(() -> new IllegalStateException("Cannot find a book"));

        Recepient recepient = Recepient
                .builder()
                .name("Jan Kowalski")
                .phone("666-888-009")
                .street("Warszawska 12")
                .city("Gdynia")
                .zipCode("80-021")
                .email("jan.kowalski@wp.pl")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recepient(recepient)
                .item(new OrderItem(panTadeusz, 16))
                .item(new OrderItem(chlopi, 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Created ORDER with id: " + response.getOrderId());

        queryOrder.findAll()
                .forEach(order -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order);
                });
    }

    private void searchCatalog() {
        findByTitle();
        findByAuthor();
        findAndUpdate();
        findByTitle();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Kolejne 365 dni", "Blanka Lipińska", 2019, new BigDecimal("19.90")));
        catalog.addBook(new CreateBookCommand("Nasz ostatni dzień", "Adam Silvera", 2017, new BigDecimal("17.90")));
        catalog.addBook(new CreateBookCommand("Obcy", "Albert Camus", 1957, new BigDecimal("29.90")));
        catalog.addBook(new CreateBookCommand("Mały Książę", "Antoine de Saint-Exupéry", 1943, new BigDecimal("25.90")));
        catalog.addBook(new CreateBookCommand("Pan Tadeusz", "Adam Mickiewicz", 1834, new BigDecimal("33.90")));
        catalog.addBook(new CreateBookCommand("Ogniem i Mieczem", "Henryk Sienkiewicz", 1884, new BigDecimal("26.90")));
        catalog.addBook(new CreateBookCommand("Chłopi", "Władysław Reymont", 1904, new BigDecimal("29.90")));
        catalog.addBook(new CreateBookCommand("Pan Wołodyjowski", "Henryk Sienkiewicz", 1912, new BigDecimal("21.90")));
    }

    private void findAndUpdate() {
        System.out.println("Updating book....");
        catalog.findOneByTitleAndAuthor("Pan Tadeusz", "Adam Mickiewicz")
                .ifPresent(book -> {
                    UpdateBookCommand command = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("Pan Tadeusz, czyli ostatni zajazd na Litwie")
                            .build();
                    UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }

    private void findByAuthor() {
        System.out.println("find by author:");
        List<Book> authors = catalog.findByAuthor(author);
        authors.stream().limit(limit).forEach(System.out::println);
    }

    private void findByTitle() {
        System.out.println("find by title:");
        List<Book> books = catalog.findByTitle(title);
        books.stream().limit(limit).forEach(System.out::println);
    }
}
