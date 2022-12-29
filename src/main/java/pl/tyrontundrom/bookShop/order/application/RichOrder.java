package pl.tyrontundrom.bookShop.order.application;

import lombok.Value;
import pl.tyrontundrom.bookShop.order.application.price.OrderPrice;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RichOrder {
    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;
    OrderPrice orderPrice;
    BigDecimal finalPrice;
}
