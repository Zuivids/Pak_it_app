package lv.pakit;

import lv.pakit.model.Fragility;
import lv.pakit.model.Product;
import lv.pakit.repo.IProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PakItAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PakItAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner testDatabase(IProductRepo productRepo) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {

				Product p1 = new Product("Telefons", "Telefona apraksts", 21, "Elektroprece", Fragility.FRAGILE);
				Product p2 = new Product("Ziepes", "Ziepju apraksts", 14, "Higēnas prece", Fragility.NON_FRAGILE);
				Product p3 = new Product("Marinēti gurķi", "Marinētu gurķu apraksts", 133, "Pārtika", Fragility.FRAGILE);

				productRepo.save(p1);
				productRepo.save(p2);
				productRepo.save(p3);

			}
		};
	}

}
