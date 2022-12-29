package pl.tyrontundrom.bookShop.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.port.QueryOrderUseCase;
import pl.tyrontundrom.bookShop.order.application.RichOrder;
import pl.tyrontundrom.bookShop.order.domain.OrderStatus;
import pl.tyrontundrom.bookShop.web.CreatedURI;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static pl.tyrontundrom.bookShop.order.application.port.ManipulateOrderUseCase.*;

@RequestMapping("/orders")
@RestController
@AllArgsConstructor
class OrdersController {

    private final ManipulateOrderUseCase manipulateOrder;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RichOrder> getAll() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RichOrder> getById(@PathVariable Long id) {
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@RequestBody PlaceOrderCommand command) {
        return manipulateOrder
                .placeOrder(command)
                .handle(
                        orderId -> ResponseEntity.created(orderURI(orderId)).build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    URI orderURI(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        OrderStatus orderStatus = OrderStatus
                .parseString(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + status));
        UpdateStatusCommand command = new UpdateStatusCommand(id, orderStatus, "admin@example.com");
        manipulateOrder.updateOrderStatus(command);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        manipulateOrder.deleteOrderById(id);
    }



}
