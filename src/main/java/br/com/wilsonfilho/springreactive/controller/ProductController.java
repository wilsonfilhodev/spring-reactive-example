package br.com.wilsonfilho.springreactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public Flux<Product> all() {
		return service.getAll();
	}

	@GetMapping("{productId}")
	public Mono<Product> byId(@PathVariable String productId) {
		return service.getById(productId);
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "{productId}/events")
	public Flux<ProductEvents> eventsOfStreams(@PathVariable String productId) {
		return service.streams(productId);
	}
}
