package br.com.wilsonfilho.springreactive.config.web;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.service.ProductService;

@Configuration
public class ProductWeb {

	@Bean
	ProductService service() {
		return new ProductService();
	}

	@Bean
	RouterFunction<ServerResponse> getAllProductRoute() {
		return route(GET("/products"), req -> ok().body(service().getAll(), Product.class)
				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!")));
	}

	@Bean
	RouterFunction<ServerResponse> getProductByIdRoute() {
		return route(GET("/products/{productId}"),
				req -> ok().body(service().getById(req.pathVariable("productId")), Product.class)
				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!")));
	}

	@Bean
	RouterFunction<ServerResponse> updateProductRoute() {
		return route(PUT("/products"), req -> req.body(toMono(Product.class))
				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!"))
				.doOnNext(service()::update).then(ok().build()));
	}
	
	@Bean
	RouterFunction<ServerResponse> saveProductRoute() {
		return route(POST("/products"), req -> req.body(toMono(Product.class))
				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!"))
				.doOnNext(service()::save).then(ok().build()));
	}
	
	@Bean
	RouterFunction<ServerResponse> deleteProductByIdRoute() {
		return route(DELETE("/products/{productId}"),
				req -> ok().body(service().delete(req.pathVariable("productId")), Product.class)
				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!")));
	}
	
//	@Bean
//	RouterFunction<ServerResponse> streamsProductByIdRoute() {
//		return route(GET("/products/{productId}/events").and(accept(MediaType.TEXT_EVENT_STREAM)),
//				req -> ok().body(service().streams(req.pathVariable("productId")), ProductEvents.class)
//				.doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!")));
//	}
	
}
