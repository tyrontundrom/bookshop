package pl.tyrontundrom.bookShop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.tyrontundrom.bookShop.catalog.domain.Book;
import pl.tyrontundrom.bookShop.catalog.domain.CatalogService;

import java.util.List;

@SpringBootApplication
public class ShopOnlineApplication {



	public static void main(String[] args) {
		SpringApplication.run(ShopOnlineApplication.class, args);
	}



}
