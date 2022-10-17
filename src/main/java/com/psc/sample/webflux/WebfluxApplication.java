package com.psc.sample.webflux;

import com.psc.sample.webflux.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Autowired
    MongoOperations mongoOperations;    //블로킹 방식으로 CRUD 가능

    @EventListener(ApplicationReadyEvent.class) //처음에는 DB를 잡기 위해 블로킹 방식으로 한 번 데이터 넣어주기
    public void doSomething() {
        Item item1 = new Item("lego", "made in usa", 20.00);
        Item item2 = new Item("lego", "made in china", 10.00);
        Item item3 = new Item("rc car", "made in usa", 40.00);
        Item item4 = new Item("rc car", "made in china", 20.00);
        Item item5 = new Item("rc car", "made in india", 15.00);
        Item item6 = new Item("drone", "made in korea", 100.00);
        mongoOperations.save(item1);
        mongoOperations.save(item2);
        mongoOperations.save(item3);
        mongoOperations.save(item4);
        mongoOperations.save(item5);
        mongoOperations.save(item6);
    }

}
