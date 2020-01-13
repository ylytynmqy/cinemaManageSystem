package com.example.cinema.bl.sales;


import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.ResponseVO;

public interface OrderService {
    /**
     * 新生成一个订单
     */
    ResponseVO addOneOrder(OrderForm orderForm);

    ResponseVO updateOrderState(int id, int state);
    /**
     * 根据user_id获得所有的订单
     */
    ResponseVO getOrdersByUser(int user_id);

    /**
     * 根据order_id获得订单
     */
    ResponseVO getOrderById(int id);

    /**
     * 取消订单
     */
    ResponseVO cancelOrder(int order_id);

    /**
     * 支付订单，使用银行卡
     */
    ResponseVO payOrderByBankCard(int order_id);

    /**
     * 使用会员卡 支付订单
     * @param order_id
     * @return
     */
    ResponseVO payOrderByVIPCard(int order_id);

    /**
     * 退票
     */
    ResponseVO refundTicket(int order_id);
}
