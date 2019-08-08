package br.com.wilsonfilho.springreactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.service.FluxProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("/products")
public class ProductController {

	private final FluxProductService service;

	public ProductController(FluxProductService service) {
		this.service = service;
	}

	@GetMapping
	public Flux<Product> all() {
		return service.all();
	}

	@GetMapping("{productId}")
	public Mono<Product> byId(@PathVariable String productId) {
		return service.byId(productId);
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "{productId}/events")
	public Flux<ProductEvents> eventsOfStreams(@PathVariable String productId) {
		return service.streams(productId);
	}
}
