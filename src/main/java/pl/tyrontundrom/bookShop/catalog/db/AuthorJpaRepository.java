package pl.tyrontundrom.bookShop.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Author;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
