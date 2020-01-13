package com.example.cinema.bl.sales;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Test
    public void getOrdersByUser(){ ;
        System.out.println("getOrderByUser(id:2):"+orderService.getOrdersByUser(2).getSuccess());
    }
    @Test
    public void getOrderById(){
        System.out.println("getOrderById:"+orderService.getOrderById(1).getSuccess());
    }
}