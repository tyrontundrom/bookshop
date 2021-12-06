package pl.tyrontundrom.bookShop.order.domain;

import lombok.Value;
import pl.tyrontundrom.bookShop.catalog.domain.Book;

@Value
public class OrderItem {
    Book book;
    int quantity;
}
