package br.com.wilsonfilho.springreactive.service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public Flux<Product> getAll() {
		return repository.findAll().switchIfEmpty(Flux.empty());
	}

	public Mono<Product> getById(String productId) {
		return repository.findById(productId);
	}

	public Mono<Product> update(Product product) {
		return repository.save(product);
	}

	public Mono<Product> save(Product product) {
		return repository.save(product);
	}

	public Mono<Product> delete(String id) {
		Mono<Product> dbProduct = getById(id);
		if (Objects.isNull(dbProduct)) {
			return Mono.empty();
		}
		return getById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull).flatMap(
				productToBeDeleted -> repository.delete(productToBeDeleted).then(Mono.just(productToBeDeleted)));
	}

	public Flux<ProductEvents> streams(String productId) {
		return getById(productId).flatMapMany(product -> {
			Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
			Flux<ProductEvents> events = Flux.fromStream(Stream.generate(() -> new ProductEvents(product, new Date())));
			return Flux.zip(interval, events).map(Tuple2::getT2);
		});
	}
}
