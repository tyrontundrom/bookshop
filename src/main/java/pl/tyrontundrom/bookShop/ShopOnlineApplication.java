package pl.tyrontundrom.bookShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
