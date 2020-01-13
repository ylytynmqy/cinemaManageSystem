package com.example.cinema.bl.sales;

import com.example.cinema.data.sales.OrderMapper;
import com.example.cinema.po.Order;
import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Override
    public ResponseVO addOneOrder(OrderForm orderForm) {
        return null;
    }

    @Override
    public ResponseVO updateOrderState(int id, int state) {
        return null;
    }

    @Override
    public ResponseVO getOrdersByUser(int user_id) {
        try{
            List<Order> orders = orderMapper.selectOrdersByUser(user_id);
            return ResponseVO.buildSuccess(orders);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getOrderByUser Failed");
        }
    }

    @Override
    public ResponseVO getOrderById(int id) {
        return null;
    }

    @Override
    public ResponseVO cancelOrder(int order_id) {
        return null;
    }

    @Override
    public ResponseVO payOrderByBankCard(int order_id) {
        return null;
    }

    @Override
    public ResponseVO payOrderByVIPCard(int order_id) {
        return null;
    }

    @Override
    public ResponseVO refundTicket(int order_id) {
        return null;
    }
}
