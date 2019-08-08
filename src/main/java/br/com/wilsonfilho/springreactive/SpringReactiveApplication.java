package br.com.wilsonfilho.springreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.wilsonfilho.springreactive.events.ProductEvents;
import br.com.wilsonfilho.springreactive.model.Product;
import br.com.wilsonfilho.springreactive.service.FluxProductService;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveApplication.class, args);
	}
	
	@Component
	public static class RouteHandles {
	   private final FluxProductService fluxProductService;

	   public RouteHandles(FluxProductService fluxProductService) {
	       this.fluxProductService = fluxProductService;
	   }

	   public Mono<ServerResponse> allProducts(ServerRequest serverRequest) {
	       return ServerResponse.ok()
	               .body(fluxProductService.all(), Product.class)
	               .doOnError(throwable -> new IllegalStateException("My godness NOOOOO!!"));
	   }

	   public Mono<ServerResponse> productById(ServerRequest serverRequest) {
	       String productId = serverRequest.pathVariable("productId");
	       return ServerResponse.ok()
	               .body(fluxProductService.byId(productId), Product.class)
	               .doOnError(throwable -> new IllegalStateException("oh boy... not againnn =(("));
	   }

	   public Mono<ServerResponse> events(ServerRequest serverRequest) {
	       String productId = serverRequest.pathVariable("productId");
	       return ServerResponse.ok()
	               .contentType(MediaType.TEXT_EVENT_STREAM)
	               .body(fluxProductService.streams(productId), ProductEvents.class)
	               .doOnError(throwable -> new IllegalStateException("I give up!! "));
	   }
	}
	
	@Bean
	RouterFunction<?> routes(RouteHandles routeHandles) {
	   return RouterFunctions.route(
	           RequestPredicates.GET("/products"), routeHandles::allProducts)
	           .andRoute(RequestPredicates.GET("/products/{productId}"), routeHandles::productById)
	           .andRoute(RequestPredicates.GET("/products/{productId}/events"), routeHandles::events);
	}

}
