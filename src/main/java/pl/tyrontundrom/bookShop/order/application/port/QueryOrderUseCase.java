package pl.tyrontundrom.bookShop.order.application.port;

import pl.tyrontundrom.bookShop.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findAll();
}
