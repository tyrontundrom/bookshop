package pl.tyrontundrom.bookShop.order.domain;

import lombok.*;
import pl.tyrontundrom.bookShop.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    private Long bookId;
    private int quantity;

}
