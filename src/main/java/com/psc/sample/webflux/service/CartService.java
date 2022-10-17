package com.psc.sample.webflux.service;

import com.psc.sample.webflux.domain.Cart;
import com.psc.sample.webflux.domain.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {

    public Mono<Cart> delToCartCount(String cartId, String id);

    public Mono<Cart> delToCartAll(String cartId, String id);

    public Mono<Cart> addToCart(String cartId, String id);
    
    public Flux<Item> itemSearchName(String name, String description, boolean isSuit); //검색조건을 주어서 검색
}
