package br.com.wilsonfilho.springreactive.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.wilsonfilho.springreactive.config.web.ProductWeb;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest
@Import(ProductWeb.class)
public class ProductWebConfigTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	private Product mockProduct;

	@Before
	public void before() {

		mockProduct = Product.builder().id("string").name("product1").description("product-description").build();

		Mockito.when(this.productService.getAll())
				.thenReturn(Flux.just(Product.builder().name("product1").description("product-description").build(),
						Product.builder().name("product2").description("product-description").build(),
						Product.builder().name("product3").description("product-description").build()));

		Mockito.when(this.productService.getById("string")).thenReturn(Mono.just(mockProduct));

		Mockito.when(this.productService.update(mockProduct)).thenReturn(Mono.just(mockProduct));

		Mockito.when(this.productService.save(mockProduct)).thenReturn(Mono.just(mockProduct));

		Mockito.when(this.productService.delete("string")).thenReturn(Mono.just(mockProduct));

		Hooks.onOperatorDebug();
	}

	@Test
	public void testShouldGetAll() {
		this.webTestClient.get().uri("/products").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
				.isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody().jsonPath("$.[0].name")
				.isEqualTo("product1").jsonPath("$.[1].description").isEqualTo("product-description")
				.jsonPath("$.[1].name").isEqualTo("product2").jsonPath("$.[1].description")
				.isEqualTo("product-description");
	}

	@Test
	public void testShouldFetchById() throws JsonProcessingException {
		String jsonBlob = "{id: 'string', name: 'product1'}";

		this.webTestClient.get().uri("/products/string").accept(MediaType.APPLICATION_JSON_UTF8).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.json(jsonBlob);
	}

	@Test
	public void update() {
		this.webTestClient.put().uri("/products").body(BodyInserters.fromObject(mockProduct)).exchange().expectStatus()
				.isOk();

//	TODO colocar	.expectBody().jsonPath("$.name").isEqualTo("product1")
	}

	@Test
	public void save() {
		this.webTestClient.post().uri("/products").body(BodyInserters.fromObject(mockProduct)).exchange().expectStatus()
				.isOk();

//	TODO colocar	.expectBody().jsonPath("$.name").isEqualTo("product1")
	}

	@Test
	public void delete() {
		this.webTestClient.delete().uri("/products/string").exchange().expectStatus().isOk().expectBody()
				.jsonPath("$.name").isEqualTo("product1");
	}

}
