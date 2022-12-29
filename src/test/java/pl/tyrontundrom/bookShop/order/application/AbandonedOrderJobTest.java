package pl.tyrontundrom.bookShop.order.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.db.BookJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.clock.Clock;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.*;

@SpringBootTest(
        properties = "app.orders.payment-period=1H"
)
@AutoConfigureTestDatabase
class AbandonedOrderJobTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    BookJpaRepository bookRepository;

    @Autowired
    ManipulateOrderService manipulateOrderService;

    @Autowired
    QueryOrderUseCase queryOrderUseCase;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    Clock.Fake clock;

    @Autowired
    AbandonedOrderJob orderJob;

    @Test
    void shouldMarkOrdersAsAbandoned() {
        // given
        Book book = givenEffectiveJava(50L);
        Long orderId = placedOrder(book.getId(), 15);

        // when
        clock.tick(Duration.ofHours(2));
        orderJob.run();

        // then
        assertEquals(OrderStatus.ABANDONED, queryOrderUseCase.findById(orderId).get().getStatus());
        assertEquals(50L, availableCopiesOf(book));
    }

    private Long placedOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(bookId, copies))
                .build();
        return manipulateOrderService.placeOrder(command).getRight();
    }

    private Recipient recipient() {
        return Recipient.builder().email("email@example.com").build();
    }

    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2006, new BigDecimal("129.90"), available));
    }

    private Long availableCopiesOf(Book effectiveJava) {
        return catalogUseCase.findById(effectiveJava.getId()).get().getAvailable();
    }

}