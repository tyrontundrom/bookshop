package pl.tyrontundrom.bookShop.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.tyrontundrom.bookShop.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @Query(" SELECT DISTINCT b FROM Book b JOIN FETCH b.authors ")
    List<Book> findAllEager();

    List<Book> findByAuthors_nameContainsIgnoreCase(String name);

    List<Book> findByTitleStartsWithIgnoreCase(String title);

    Optional<Book> findDistinctFirstByTitle(String title);

    @Query(
            " SELECT b FROM Book b JOIN b.authors a " +
                    " WHERE " +
                    " lower(a.name) LIKE lower(concat('%', :name,'%')) "
    )
    List<Book> findByAuthor(@Param("name") String name);

    @Query(
            " SELECT b FROM Book b JOIN b.authors a " +
                    " WHERE " +
                    " lower(b.title) LIKE lower(concat('%', :book_title,'%')) " +
                    " AND lower(a.name) LIKE lower(concat('%', :name,'%')) "
    )
    List<Book> findByTitleAndAuthor(@Param("book_title") String title, @Param("name") String author);
}
