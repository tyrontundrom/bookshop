package pl.tyrontundrom.bookShop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.tyrontundrom.bookShop.commons.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.commons.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.tyrontundrom.bookShop.commons.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import pl.tyrontundrom.bookShop.commons.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import pl.tyrontundrom.bookShop.commons.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.Recipient;


import java.math.BigDecimal;
import java.util.List;

import static pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final Long limit;

    public ApplicationStartup(CatalogUseCase catalog,
                              ManipulateOrderUseCase placeOrder,
                              QueryOrderUseCase queryOrder,
                              @Value("${bookShop.catalog.query}") String title,
                              @Value("${bookShop.catalog.limit}") Long limit
    ) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
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

        Recipient recipient = Recipient
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
                .recipient(recipient)
                .item(new OrderItem(panTadeusz.getId(), 16))
                .item(new OrderItem(chlopi.getId(), 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);

        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    private void searchCatalog() {
        findByTitle();
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
        catalog.findOneByTitleAndAuthor("Pan Tadeusz", "Adam Mickiewicz")
                .ifPresent(book -> {
                    UpdateBookCommand command = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("Pan Tadeusz, czyli Ostatni Zajazd na Litwie")
                            .build();
                    UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }


    private void findByTitle() {
        System.out.println("find by title:");
        List<Book> books = catalog.findByTitle(title);
        books.stream().limit(limit).forEach(System.out::println);
    }
}
