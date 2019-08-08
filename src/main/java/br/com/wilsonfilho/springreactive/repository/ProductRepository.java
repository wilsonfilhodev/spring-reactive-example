package br.com.wilsonfilho.springreactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.wilsonfilho.springreactive.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
