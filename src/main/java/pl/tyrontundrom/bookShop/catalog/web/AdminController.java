package pl.tyrontundrom.bookShop.catalog.web;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.db.AuthorJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Author;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
class AdminController {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;


    @PostMapping("/data")
    @Transactional
    public void initialize() {
        initData();
        placeOrder();
    }

    private void placeOrder() {
        Book panTadeusz = catalog.findOneByTitle("Pan Tadeusz")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book chlopi = catalog.findOneByTitle("Java Puzzlers")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book"));

        Recipient recipient = Recipient
                .builder()
                .name("Jan Kowalski")
                .phone("666-888-009")
                .street("Warszawska 12")
                .city("Gdynia")
                .zipCode("80-021")
                .email("jan.kowalski@wp.pl")
                .build();

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(panTadeusz.getId(), 16))
                .item(new OrderItem(chlopi.getId(), 7))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);

        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }


    private void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        Author blanka = new Author("Blanka", "Lipińska");
        Author adam = new Author("Adam", "Silvera");
        Author albert = new Author("Albert", "Camus");
        Author antoine = new Author("Antione", "de Saint-Exupery");
        Author adamM = new Author("Adam", "Mickiewicz");
        Author henryk = new Author("Henryk", "Sienkiewicz");
        Author wladyslaw = new Author("Władysław", "Reymont");
        authorJpaRepository.save(joshua);
        authorJpaRepository.save(neal);
        authorJpaRepository.save(blanka);
        authorJpaRepository.save(adam);
        authorJpaRepository.save(albert);
        authorJpaRepository.save(antoine);
        authorJpaRepository.save(adamM);
        authorJpaRepository.save(henryk);
        authorJpaRepository.save(wladyslaw);
        CatalogUseCase.CreateBookCommand effectiveJava = new CatalogUseCase.CreateBookCommand("Effective Java", Set.of(joshua.getId()), 2005, new BigDecimal("79.00"));
        CatalogUseCase.CreateBookCommand javaPuzzlers = new CatalogUseCase.CreateBookCommand("Java Puzzlers", Set.of(joshua.getId(), neal.getId()), 2018, new BigDecimal("99.00"));
        CatalogUseCase.CreateBookCommand kolejne365Dni = new CatalogUseCase.CreateBookCommand("Kolejne 365 dni", Set.of(blanka.getId()), 2019, new BigDecimal("19.90"));
        CatalogUseCase.CreateBookCommand naszOstatniDzien = new CatalogUseCase.CreateBookCommand("Nasz ostatni dzień", Set.of(adam.getId()), 2017, new BigDecimal("17.90"));
        CatalogUseCase.CreateBookCommand obcy = new CatalogUseCase.CreateBookCommand("Obcy", Set.of(albert.getId()), 1957, new BigDecimal("29.90"));
        CatalogUseCase.CreateBookCommand malyKsiaze = new CatalogUseCase.CreateBookCommand("Mały Książę", Set.of(antoine.getId()), 1943, new BigDecimal("25.90"));
        CatalogUseCase.CreateBookCommand panTadeusz = new CatalogUseCase.CreateBookCommand("Pan Tadeusz", Set.of(adamM.getId()), 1834, new BigDecimal("33.90"));
        CatalogUseCase.CreateBookCommand ogniemIMieczem = new CatalogUseCase.CreateBookCommand("Ogniem i Mieczem", Set.of(henryk.getId()), 1884, new BigDecimal("26.90"));
        CatalogUseCase.CreateBookCommand chlopi = new CatalogUseCase.CreateBookCommand("Chłopi", Set.of(wladyslaw.getId()), 1904, new BigDecimal("29.90"));
        CatalogUseCase.CreateBookCommand panWolodyjowski = new CatalogUseCase.CreateBookCommand("Pan Wołodyjowski", Set.of(henryk.getId()), 1912, new BigDecimal("21.90"));
        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);
        catalog.addBook(kolejne365Dni);
        catalog.addBook(naszOstatniDzien);
        catalog.addBook(obcy);
        catalog.addBook(malyKsiaze);
        catalog.addBook(panTadeusz);
        catalog.addBook(ogniemIMieczem);
        catalog.addBook(chlopi);
        catalog.addBook(panWolodyjowski);
    }
}
