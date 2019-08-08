package br.com.wilsonfilho.springreactive.service;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class FluxProductService {

	private final ProductRepository repository;

	public FluxProductService(ProductRepository repository) {
		this.repository = repository;
	}

	public Flux<Product> all() {
		return repository.findAll();
	}

	public Mono<Product> byId(String productId) {
		return repository.findById(productId);
	}

	public Flux<ProductEvents> streams(String productId) {
		return byId(productId).flatMapMany(product -> {
			Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
			Flux<ProductEvents> events = Flux.fromStream(Stream.generate(() -> new ProductEvents(product, new Date())));
			return Flux.zip(interval, events).map(Tuple2::getT2);
		});
	}
}
