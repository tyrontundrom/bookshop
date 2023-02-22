package pl.tyrontundrom.bookShop.order.application;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.DirtiesContext;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.db.BookJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.Delivery;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookRepository;

    @Autowired
    ManipulateOrderService service;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    QueryOrderUseCase queryOrderUseCase;

    @Test
    void userCanPlaceOrder() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        Book jcip = givenJavaConcurrencyInPractice(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), 15))
                .item(new ManipulateOrderUseCase.OrderItemCommand(jcip.getId(), 10))
                .build();

        // when
        PlaceOrderResponse response = service.placeOrder(command);

        // then
        assertTrue(response.isSuccess());
        assertEquals(35L, availableCopiesOf(effectiveJava));
        assertEquals(40L, availableCopiesOf(jcip));
    }

    @Test
    void userCanRevokeOrder() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.com";
        Long orderId = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        // when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, user(recipient));
        service.updateOrderStatus(command);

        // then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    void userCantOrderMoreBooksThanAvailable() {
        // given
        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), 10))
                .build();

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.placeOrder(command));

        // then
        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested"));
    }

    @Test
    void userCannotRevokePaidOrder() {
        // user nie może wycofać już opłaconego zamówienia

        // given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placedOrder(effectiveJava.getId(), 11);
        assertEquals(39L, availableCopiesOf(effectiveJava));
        // when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, userAdmin());
        service.updateOrderStatus(command);
        UpdateStatusCommand commandUpdate = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, userAdmin());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> service.updateOrderStatus(commandUpdate));

        // then
        assertTrue(exception.getMessage().contains("Unable to mark " + OrderStatus.PAID + " order as " + OrderStatus.CANCELED));
        assertEquals(39L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.PAID, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    void userCannotRevokeShippedOrder() {
        // user nie może wycofać wysłanego zamówienia

        // given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placedOrder(effectiveJava.getId(), 11);
        assertEquals(39L, availableCopiesOf(effectiveJava));
        // when
        UpdateStatusCommand paidCommand = new UpdateStatusCommand(orderId, OrderStatus.PAID, userAdmin());
        UpdateStatusCommand shippedCommand = new UpdateStatusCommand(orderId, OrderStatus.SHIPPED, userAdmin());
        UpdateStatusCommand canceledCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, userAdmin());
        service.updateOrderStatus(paidCommand);
        service.updateOrderStatus(shippedCommand);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> service.updateOrderStatus(canceledCommand));
        // then
        assertTrue(exception.getMessage().contains("Unable to mark " + OrderStatus.SHIPPED + " order as " + OrderStatus.CANCELED));
        assertEquals(39L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.SHIPPED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Disabled
    void userCannotOrderNoExistBooks() {
        // user nie może zamówić nie istniejących książek
        Long orderId = placedOrder(99L, 12);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(orderId, 15))
                .build();

        PlaceOrderResponse response = service.placeOrder(command);

        // then
        assertTrue(!response.isSuccess());

    }

    @Test
    void userCannotOrderNegativeNumberOfBooks() {
        // user nie może zamówić ujemnej liczby książek
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), -5))
                .build();

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.placeOrder(command));

        // then
        assertTrue(exception.getMessage().contains("You don't order negative "));
        assertEquals(50L, availableCopiesOf(effectiveJava));

    }

    @Test
    void userCannotRevokeOtherUserOrder() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String adam = "adam@example.com";
        String marek = "marek@example.com";
        Long orderId = placedOrder(effectiveJava.getId(), 15, adam);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        // when
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, user(marek));
        service.updateOrderStatus(updateStatusCommand);

        // then
        assertEquals(35L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
        // TODO: 2022-12-26 :  poprawić w module security
    void adminCanRevokeOtherUserOrder() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String marek = "marek@example.com";
        Long orderId = placedOrder(effectiveJava.getId(), 15, marek);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        // when
        String admin = "admin@example.com";
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, userAdmin());
        service.updateOrderStatus(updateStatusCommand);

        // then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    void adminCanMarkOrderAsPaid() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.com";
        Long orderId = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        // when
        String admin = "admin@example.com";
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, userAdmin());
        service.updateOrderStatus(command);

        // then
        assertEquals(35L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.PAID, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    void shippingCostsAreAddedToTotalOrderPrice() {
        // given
        Book book = givenBook(50L, "49.90");

        // when
        Long orderId = placedOrder(book.getId(), 1);

        // then
        assertEquals("59.80", orderOf(orderId).getFinalPrice().toPlainString());
    }


    @Test
    void shippingCostsAreDiscountedOver100zlotys() {
        // given
        Book book = givenBook(50L, "49.90");

        // when
        Long orderId = placedOrder(book.getId(), 3);

        // then
        RichOrder order = orderOf(orderId);
        assertEquals("149.70", order.getFinalPrice().toPlainString());
        assertEquals("149.70", order.getOrderPrice().getItemsPrice().toPlainString());
    }


    @Test
    void cheapestBookIsHalfPricedWhenTotalOver200zlotys() {
        // given
        Book book = givenBook(50L, "49.90");

        // when
        Long orderId = placedOrder(book.getId(), 5);

        // then
        RichOrder order = orderOf(orderId);
        assertEquals("224.55", order.getFinalPrice().toPlainString());
    }


    @Test
    void cheapestBookIsFreeWhenTotalOver400zlotys() {
        // given
        Book book = givenBook(50L, "49.90");

        // when
        Long orderId = placedOrder(book.getId(), 10);

        // then
        assertEquals("449.10", orderOf(orderId).getFinalPrice().toPlainString());
    }

    private Long placedOrder(Long bookId, int copies, String recipient) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .item(new ManipulateOrderUseCase.OrderItemCommand(bookId, copies))
                .delivery(Delivery.COURIER)
                .build();
        return service.placeOrder(command).getRight();
    }

    private RichOrder orderOf(Long orderId) {
        return queryOrderUseCase.findById(orderId).get();
    }

    private Long placedOrder(Long bookId, int copies) {
        return placedOrder(bookId, copies, "john@example.com");
    }

    private Long availableCopiesOf(Book effectiveJava) {
        return catalogUseCase.findById(effectiveJava.getId()).get().getAvailable();
    }

    private Book givenBook(long available, String price) {
        return bookRepository.save(new Book("Effective Java", 2006, new BigDecimal(price), available));
    }

    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2006, new BigDecimal("129.90"), available));
    }

    private Book givenJavaConcurrencyInPractice(long available) {
        return bookRepository.save(new Book("Java Concurrency in Practice", 2005, new BigDecimal("99.90"), available));
    }

    private User user(String email) {
        return new User(email, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private User userAdmin() {
        return new User("admin", "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    private Recipient recipient() {
        return Recipient.builder().email("email@example.com").build();
    }

    private Recipient recipient(String email) {
        return Recipient.builder().email(email).build();
    }


}