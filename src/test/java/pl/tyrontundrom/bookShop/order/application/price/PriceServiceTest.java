package pl.tyrontundrom.bookShop.order.application.price;

import org.junit.jupiter.api.Test;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.RichOrder;
import pl.tyrontundrom.bookShop.order.domain.Order;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PriceServiceTest {

    PriceService priceService = new PriceService();

    @Test
    void calculatesTotalPriceOfEmptyOrder() {
        // given
        Order order = Order
                .builder()
                .build();

        // when
        OrderPrice price = priceService.calculatePrice(order);

        // then
        assertEquals(BigDecimal.ZERO, price.finalPrice());
    }

    @Test
    void calculatesTotalPrice() {
        // given
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setPrice(new BigDecimal("12.50"));
        book2.setPrice(new BigDecimal("33.00"));

        Order order = Order
                .builder()
                .item(new OrderItem(book1, 2))
                .item(new OrderItem(book2, 5))
                .build();

        // when
        OrderPrice price = priceService.calculatePrice(order);

        // then
        assertEquals(new BigDecimal("190.00"), price.finalPrice());
        assertEquals(new BigDecimal("190.00"), price.getItemsPrice());
    }

}