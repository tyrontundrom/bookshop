package pl.tyrontundrom.bookShop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.tyrontundrom.bookShop.catalog.domain.BestsellerCatalogRepository;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogRepository;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogService;
import pl.tyrontundrom.bookShop.catalog.infrastructure.SchoolCatalogRepository;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ShopOnlineApplication {



	public static void main(String[] args) {
		SpringApplication.run(ShopOnlineApplication.class, args);
	}

	// Losowe wybór implementacji repozytoriów.
//	@Bean
//	CatalogRepository catalogRepository() {
//		Random random = new Random();
//		if (random.nextBoolean()) {
//			System.out.println("Wybieram szkolne lektury");
//			return new SchoolCatalogRepository();
//		} else {
//			System.out.println("Wybieram bestsellery");
//			return new BestsellerCatalogRepository();
//		}
//
//	}


}
