package pl.tyrontundrom.bookShop.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tyrontundrom.bookShop.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
