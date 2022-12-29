package pl.tyrontundrom.bookShop.order.application;

import org.junit.jupiter.api.Test;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
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

class RichOrderTest {

//    @Test
//    void calculatesTotalPriceOfEmptyOrder() {
//        // given
//        RichOrder order = new RichOrder(
//                1L,
//                OrderStatus.NEW,
//                Collections.emptySet(),
//                Recipient.builder().build(),
//                LocalDateTime.now()
//        );
//
//        // when
//        BigDecimal price = order.totalPrice();
//
//        // then
//        assertEquals(BigDecimal.ZERO, price);
//    }
//
//    @Test
//    void calculatesTotalPrice() {
//        // given
//        Book book1 = new Book();
//        Book book2 = new Book();
//        book1.setPrice(new BigDecimal("12.50"));
//        book2.setPrice(new BigDecimal("33.00"));
//        Set<OrderItem> items = new HashSet<>(
//                Arrays.asList(
//                        new OrderItem(book1, 2),
//                        new OrderItem(book2, 5)
//                )
//        );
//        RichOrder order = new RichOrder(
//                1L,
//                OrderStatus.NEW,
//                items,
//                Recipient.builder().build(),
//                LocalDateTime.now()
//        );
//
//        // when
//        BigDecimal price = order.totalPrice();
//
//        // then
//        assertEquals(new BigDecimal("190.00"), price);
//    }

}