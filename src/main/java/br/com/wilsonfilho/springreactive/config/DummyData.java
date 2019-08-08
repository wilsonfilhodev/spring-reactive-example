package br.com.wilsonfilho.springreactive.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.repository.ProductRepository;
import reactor.core.publisher.Flux;

@Component
class DummyData implements CommandLineRunner {

	private final ProductRepository repository;

	DummyData(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll()
				.thenMany(Flux
						.just("Notebook Dell 5570", "Mouse Microsoft T3", "Monitor LCD 15.6",
								"Roteador Intelbras WRN301", "Headphone Gamer")
						.map(name -> new Product(UUID.randomUUID().toString(), name)).flatMap(repository::save))
				.subscribe(System.out::println);
	}
}
