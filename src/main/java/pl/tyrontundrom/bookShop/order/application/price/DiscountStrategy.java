package pl.tyrontundrom.bookShop.order.application.price;

import pl.tyrontundrom.bookShop.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
