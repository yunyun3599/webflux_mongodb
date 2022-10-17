package com.psc.sample.webflux.service;

import com.psc.sample.webflux.domain.Cart;
import com.psc.sample.webflux.domain.Item;
import com.psc.sample.webflux.domain.vo.CartItem;
import com.psc.sample.webflux.repository.CartReactiveRepository;
import com.psc.sample.webflux.repository.ItemReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{

    final ItemReactiveRepository itemReactiveRepository;
    final CartReactiveRepository cartReactiveRepository;

    @Override
    public Mono<Cart> delToCartCount(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                        cart.getCartItems().stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                                .map(cartItem -> {
                                    if (cartItem.isOne()) {
                                        cart.removeItem(cartItem);
                                    } else {
                                        cartItem.decrement();
                                    }
                                    return Mono.just(cart);
                                }).orElseGet(() -> {
                                    return Mono.empty();
                                })).flatMap(cart -> cartReactiveRepository.save(cart));
    }


    @Override
    public Mono<Cart> delToCartAll(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                        cart.getCartItems().stream()
                                .filter(cartItem ->
                                        cartItem.getItem().getId().equals(id)).findAny()
                                .map(cartItem -> {
                                    cart.removeItem(cartItem);
                                    return Mono.just(cart);
                                }).orElseGet(() -> {
                                    return Mono.empty();
                                })).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> addToCart(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                        cart.getCartItems().stream()
                                .filter(cartItem ->
                                        cartItem.getItem().getId().equals(id)).findAny()
                                .map(cartItem -> {
                                    cartItem.increment();
                                    return Mono.just(cart);
                                }).orElseGet(() ->
                                        itemReactiveRepository.findById(id)
                                                .map(cartItem -> new CartItem(cartItem))
                                                .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                                .map(carItem -> cart)))
                .flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Flux<Item> itemSearchName(String name, String description, boolean isSuit) {

        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (
                isSuit
                ? ExampleMatcher.matchingAll().withIgnorePaths("price") //가격은 matching되나 확인 안함
                : ExampleMatcher.matching()
                        .withMatcher("name", contains())
                        .withMatcher("description", contains())
                        .withIgnorePaths("price")
                );

        Example<Item> itemExample = Example.of(item, matcher);

        return itemReactiveRepository.findAll(itemExample);
    }
}
