package pl.tyrontundrom.bookShop.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tyrontundrom.bookShop.order.domain.Order;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime timestamp);
//    List<Order> findByStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime timestamp);
}
