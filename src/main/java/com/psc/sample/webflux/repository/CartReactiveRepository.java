package com.psc.sample.webflux.repository;


import com.psc.sample.webflux.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartReactiveRepository extends ReactiveCrudRepository<Cart, String> {

}
