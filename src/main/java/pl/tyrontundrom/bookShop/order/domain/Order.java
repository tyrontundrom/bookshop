package pl.tyrontundrom.bookShop.order.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {

    private Long id;

    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.NEW;

    private List<OrderItem> items;

    private Recipient recipient;

    private LocalDateTime createdAt;


}
