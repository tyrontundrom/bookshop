package pl.tyrontundrom.bookShop.catalog.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogInitializerUseCase;
import pl.tyrontundrom.bookShop.catalog.application.port.CatalogUseCase;
import pl.tyrontundrom.bookShop.catalog.db.AuthorJpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Author;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.domain.OrderItem;
import pl.tyrontundrom.bookShop.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin")
@AllArgsConstructor
class AdminController {

    private final CatalogInitializerUseCase initializer;

    @PostMapping("/initialization")
    @Transactional
    public void initialize() {
        initializer.initialize();
    }


}
