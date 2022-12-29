package pl.tyrontundrom.bookShop.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum Delivery {
    SELF_PICKUP(BigDecimal.ZERO),
    COURIER(new BigDecimal("9.90"));

    private BigDecimal price;
}
