package pl.tyrontundrom.bookShop.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tyrontundrom.bookShop.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
