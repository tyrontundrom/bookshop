package pl.tyrontundrom.bookShop.order.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tyrontundrom.bookShop.clock.Clock;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import pl.tyrontundrom.bookShop.order.db.OrderJpaRepository;
import pl.tyrontundrom.bookShop.order.domain.Order;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
class AbandonedOrderJob {

    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase manipulateOrderUseCase;
    private final OrderProperties properties;
    private final Clock clock;

    @Transactional
    @Scheduled(cron = "${app.orders.abandon-cron}")
    public void run() {
        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThan = clock.now().minus(paymentPeriod);
        List<Order> orders = repository.findByOrderStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThan);
        log.info("abandoned " + orders.size());
        orders.forEach(order -> {
            // TODO: 2022-12-15 naprawić w module security
            UpdateStatusCommand command = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, "admin@example.com");
            manipulateOrderUseCase.updateOrderStatus(command);
        });
    }
}

