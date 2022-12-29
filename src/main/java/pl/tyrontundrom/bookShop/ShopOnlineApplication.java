package pl.tyrontundrom.bookShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import pl.tyrontundrom.bookShop.order.application.OrderProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(OrderProperties.class)
public class ShopOnlineApplication {



	public static void main(String[] args) {
		SpringApplication.run(ShopOnlineApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplateBuilder().build();
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

