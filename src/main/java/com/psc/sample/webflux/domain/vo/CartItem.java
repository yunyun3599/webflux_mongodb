package com.psc.sample.webflux.domain.vo;

import com.psc.sample.webflux.domain.Item;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Item item;
    private int quantity;

    public void increment() {
        this.quantity++;
    }

    public void decrement() {
        this.quantity--;
    }

    public boolean isOne() {
        if(this.quantity == 1) {
            return true;
        }
        return false;
    }
}
