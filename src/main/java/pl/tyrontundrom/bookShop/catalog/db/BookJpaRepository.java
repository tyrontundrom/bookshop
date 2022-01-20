package pl.tyrontundrom.bookShop.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.tyrontundrom.bookShop.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(String firstName, String lastName);

    List<Book> findByTitleStartsWithIgnoreCase(String title);

    Optional<Book> findDistinctFirstByTitle(String title);

    @Query(
            " SELECT b FROM Book b JOIN b.authors a " +
                    " WHERE " +
                    " lower(a.firstName) LIKE lower(concat('%', :name,'%')) " +
                    " OR lower(a.lastName) LIKE lower(concat('%', :name,'%')) "
    )
    List<Book> findByAuthor(@Param("name") String name);


}
