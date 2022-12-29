package pl.tyrontundrom.bookShop.order.application.price;

import pl.tyrontundrom.bookShop.order.domain.Order;

import java.math.BigDecimal;

class DeliveryDiscountStrategy implements DiscountStrategy {

    private static final BigDecimal TRESHHOLD = BigDecimal.valueOf(100);

    @Override
    public BigDecimal calculate(Order order) {
        if (order.getItemsPrice().compareTo(TRESHHOLD) >= 0) {
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}
