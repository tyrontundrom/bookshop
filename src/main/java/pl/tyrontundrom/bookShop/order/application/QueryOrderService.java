package pl.tyrontundrom.bookShop.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tyrontundrom.bookShop.catalog.db.BookJpaRepository;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.price.OrderPrice;
import pl.tyrontundrom.bookShop.order.application.price.PriceService;
import pl.tyrontundrom.bookShop.order.db.OrderJpaRepository;
import pl.tyrontundrom.bookShop.order.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository repository;
    private final PriceService priceService;

    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<RichOrder> findById(Long id) {
        return repository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {
        OrderPrice orderPrice = priceService.calculatePrice(order);
        return new RichOrder(
                order.getId(),
                order.getOrderStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finalPrice()
        );
    }

}
