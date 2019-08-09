package br.com.wilsonfilho.springreactive.service;

import static org.mockito.BDDMockito.given;

import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductService.class)
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	@Test
	public void getAll() {
		given(this.productRepository.findAll())
				.willReturn(Flux.just(Product.builder().name("product1").description("product-description").build(),
						Product.builder().name("product2").description("product-description").build(),
						Product.builder().name("product3").description("product-description").build()));

		Flux<Product> products = productService.getAll();

		Predicate<Product> match = product -> products.any(saveItem -> saveItem.equals(product)).block();

		StepVerifier.create(products).expectNextMatches(match).expectNextMatches(match).expectNextMatches(match)
				.verifyComplete();
	}

	@Test
	public void getById() {
		Product productMock = Product.builder().id("string").name("product1").description("product-description")
				.build();

		given(this.productRepository.findById("string")).willReturn(Mono.just(productMock));

		Mono<Product> productMono = this.productService.getById("string");

		StepVerifier.create(productMono).expectNextMatches(product -> product.getName().equalsIgnoreCase("product1"))
				.verifyComplete();
	}

	@Test
	public void save() {
		Product productMock = Product.builder().name("product1").description("product-description").build();

		given(this.productRepository.save(productMock)).willReturn(Mono.just(productMock));

		Mono<Product> productMono = this.productService.save(productMock);

		StepVerifier.create(productMono).expectNextMatches(saved -> StringUtils.hasText(saved.getName()))
				.verifyComplete();
	}

	@Test
	public void update() {
		Product productMock = Product.builder().id("string").name("product1").description("product-description")
				.build();

		given(this.productRepository.save(productMock)).willReturn(Mono.just(productMock));

		Mono<Product> productMono = this.productService.update(productMock);

		StepVerifier.create(productMono).expectNextMatches(product -> product.getName().equalsIgnoreCase("product1"))
				.verifyComplete();
	}

	@Test
	public void deleteAndReturnElement() {
		Product productMock = Product.builder().id("string").name("product1").description("product-description")
				.build();

		given(this.productRepository.delete(productMock)).willReturn(Mono.empty());
		given(this.productRepository.findById("string")).willReturn(Mono.just(productMock));

		Mono<Product> deleted = this.productService.delete(productMock.getId());

		StepVerifier.create(deleted).expectNextMatches(product -> product.getName().equalsIgnoreCase("product1"))
				.verifyComplete();
	}

	@Test
	public void deleteAndReturnEmpty() {
		Product productMock = Product.builder().id("string").name("product1").description("product-description")
				.build();

		given(this.productRepository.delete(productMock)).willReturn(Mono.empty());

		Mono<Product> deleted = this.productService.delete(productMock.getId());

		StepVerifier.create(deleted).expectComplete().verify();
	}
	
	@Test
	public void streams() {
		Product productMock = Product.builder().id("string").name("product1").description("product-description")
				.build();

		given(this.productRepository.findById("string")).willReturn(Mono.just(productMock));

		Flux<ProductEvents> productEvents = this.productService.streams("string");

		StepVerifier.create(productEvents)
			.expectNextMatches(product -> product.getProduct().getName().equalsIgnoreCase("product1"));
	}

}
