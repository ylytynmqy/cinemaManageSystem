package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.OrderService;
import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    //新生成一个订单
    @PostMapping("/order/add")
    public ResponseVO addOneOrder(@RequestBody OrderForm orderForm){
        return orderService.addOneOrder(orderForm);
    }

    @PostMapping("/order/state/update")
    public ResponseVO updateOrderState(@RequestParam int id,int state){
        return orderService.updateOrderState(id,state);
    }

    //根据用户获得订单
    @RequestMapping(value = "/order/all/{user_id}",method= RequestMethod.GET)
    public ResponseVO getAllOrderByUserId(@PathVariable int user_id){
        return orderService.getOrdersByUser(user_id);
    }

    @RequestMapping(value = "/order/get/{id}",method= RequestMethod.GET)
    public ResponseVO getOrderById(@PathVariable int id){
        return orderService.getOrderById(id);
    }

    //取消订单
    @RequestMapping(value = "/order/cancel/{order_id}",method = RequestMethod.POST)
    public ResponseVO cancelOrder(@PathVariable int order_id){
        return orderService.cancelOrder(order_id);
    }

    //支付订单,使用银行卡
    @RequestMapping(value = "/order/pay/bankcard/{order_id}",method = RequestMethod.POST)
    public ResponseVO payOrder(@PathVariable int order_id){
        return orderService.payOrderByBankCard(order_id);
    }

    //支付订单,使用会员卡
    @RequestMapping(value = "/order/pay/vipcard/{order_id}",method = RequestMethod.POST)
    public ResponseVO payOrderByVIPCard(@PathVariable int order_id){
        return orderService.payOrderByVIPCard(order_id);
    }

    //退票
    @RequestMapping(value = "/order/refund/{order_id}",method = RequestMethod.POST)
    public ResponseVO refundTicket(@PathVariable int order_id){
        return orderService.refundTicket(order_id);
    }
}
