package pl.tyrontundrom.bookShop.order.application.port;

import pl.tyrontundrom.bookShop.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}
