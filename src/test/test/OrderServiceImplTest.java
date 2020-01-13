package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.sales.OrderService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.po.Order;
import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.ResponseVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class OrderServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private OrderService orderService;

    @LocalServerPort
    private int port;
    private URL base;
//    @Before
//    public void setUp() throws Exception {
//        String url = String.format("http://localhost:%d/", port);
//        System.out.println(String.format("port is : [%d]", port));
//        this.base = new URL(url);
//    }

    @Test
    @Transactional
    public void addOneOrderTest(){
        System.out.println("-------测试添加order------");
        OrderForm orderForm = new OrderForm();
        orderForm.setPayMethod(0);
        orderForm.setState(0);
        orderForm.setTotal(40);
        orderForm.setType(1);
        orderForm.setDescription("1");
        orderForm.setUser_id(2);
        ResponseVO vo = orderService.addOneOrder(orderForm);
        if(vo.getSuccess()){
            System.out.println("---------pass---------");
        }else{
            System.out.println("---------fail---------");
        }
    }

    @Test
    @Transactional
    public void payOrderByVIPCard() {
        System.out.println("-------测试用会员卡稍后支付------");

        ResponseVO vo = orderService.payOrderByVIPCard(6);
        if(vo.getSuccess()){
            System.out.println("---------pass---------");
        }else{
            System.out.println("---------fail---------");
        }
    }

    @Test
    @Transactional
    public void refundTicket() {
        System.out.println("----------测试退票----------");
        Order order = new Order();
        ResponseVO vo = orderService.refundTicket(1);
        if(vo.getSuccess()){
            System.out.println("---------pass---------");
        }else{
            System.out.println("---------fail---------");
            System.out.println(vo.getMessage());
        }
    }
}
