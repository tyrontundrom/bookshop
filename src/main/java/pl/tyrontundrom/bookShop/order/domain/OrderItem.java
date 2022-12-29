package pl.tyrontundrom.bookShop.order.domain;

import lombok.*;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.jpa.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private int quantity;

}
