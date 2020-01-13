package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.OrderService;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.RefundMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.OrderMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.RefundStrategyForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    RefundMapper refundMapper;

    @Override
    public ResponseVO addOneOrder(OrderForm orderForm) {
        try {
            Order order = new Order(orderForm);
            orderMapper.insertOrder(order);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("插入新的订单失败");
        }
    }
    @Override
    public ResponseVO updateOrderState(int id, int state) {
        try{
            orderMapper.updateOrderState(id,state);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("更新订单状态失败");
        }
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
    public ResponseVO getOrderById(int id){
        try{
            Order order=orderMapper.selectOrderById(id);
            return ResponseVO.buildSuccess(order);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getOrderById Failed");
        }
    }

    @Override
    /*取消订单*/
    public ResponseVO cancelOrder(int order_id) {
        try{
            Order order = orderMapper.selectOrderById(order_id);
            //更新订单状态
            orderMapper.updateOrderState(order_id,2);
            //如果是票，更新票的状态，注意退票策略
            if(order.getType()==0){
                String[] ticketIds = order.getDescription().split(",");
                for(int i=0;i<ticketIds.length;i++){
                    Ticket ticket = ticketMapper.selectTicketById(Integer.parseInt(ticketIds[i]));
                    ticketMapper.updateTicketState(ticket.getId(),2);
                }

            }
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("cancelOrder Failed");
        }
    }

    @Override
    /*支付订单,银行卡*/
    public ResponseVO payOrderByBankCard(int order_id) {
        try{
            Order order = orderMapper.selectOrderById(order_id);
            //更新订单状态
            orderMapper.updateOrderState(order_id,1);
            orderMapper.updateEndTime(order_id,new Timestamp(System.currentTimeMillis()));
            //6.11增加，更新支付方式
            orderMapper.updatePayMethod(order_id,0);
            //如果是票，更新票的状态
            if(order.getType()==0){
                String[] ticketIds = order.getDescription().split(",");
                for(int i=0;i<ticketIds.length;i++){
                    Ticket ticket = ticketMapper.selectTicketById(Integer.parseInt(ticketIds[i]));
                    ticketMapper.updateTicketState(ticket.getId(),1);
                }
            }
            //如果是买卡，vip_card，添加一条
            else if(order.getType()==1){
                VIPCard vipCard = new VIPCard();
                vipCard.setBalance(0);
                vipCard.setJoinDate(order.getJoin_time());
                vipCard.setUserId(order.getUser_id());
                vipCardMapper.insertOneCard(vipCard);
            }
            //如果是充卡，vip_card,修改balance
            //优惠策略
            else if(order.getType()==2){
                int vipId = Integer.parseInt(order.getDescription()); //卡号
                VIPCard vipCard = vipCardMapper.selectCardById(vipId);
                int cardType = vipCard.getType();
                VIPCardKind vipCardKind = vipCardMapper.selectVIPCardKindById(cardType);
                int target = vipCardKind.getTargetAmount();
                int bonus = vipCardKind.getBonusAmount();
                double orgin = vipCard.getBalance();
                double total = order.getTotal();
                double newBalance = orgin +  (total/target)*bonus+total;
                vipCardMapper.updateCardBalance(vipId,newBalance);
            }
            else{
                return ResponseVO.buildFailure("订单种类识别失败");
            }
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("payOrder failed");
        }
    }

    @Override
    //使用会员卡支付订单
    public ResponseVO payOrderByVIPCard(int order_id) {
        Order order = orderMapper.selectOrderById(order_id);
        //更新订单状态
        orderMapper.updateOrderState(order_id,1);
        orderMapper.updateEndTime(order_id,new Timestamp(System.currentTimeMillis()));
        //6.11增加，更新支付方式
        orderMapper.updatePayMethod(order_id,1);
        //如果是票，更新票的状态
        if(order.getType()==0){
            String[] ticketIds = order.getDescription().split(",");
            for(int i=0;i<ticketIds.length;i++){
                Ticket ticket = ticketMapper.selectTicketById(Integer.parseInt(ticketIds[i]));
                ticketMapper.updateTicketState(ticket.getId(),1);
            }
        }else{
            return ResponseVO.buildFailure("该订单不能用会员卡支付");
        }
        //会员卡扣钱，验证余额可不可用，使用付款折扣--改成确认订单的时候做
        int userId = order.getUser_id();
        VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
        // int cardType = vipCard.getType();
        // VIPCardKind vipCardKind = vipCardMapper.selectVIPCardKindById(cardType);
        //double discount = vipCardKind.getPercent();
        double balance = vipCard.getBalance();
        double total = order.getTotal();
        if(total>=balance){
            return ResponseVO.buildFailure("会员卡余额不足");
        }
        vipCardMapper.updateCardBalance(vipCard.getId(),balance-total);
        return ResponseVO.buildSuccess();
    }

    @Override
    /*退票*/
    public ResponseVO refundTicket(int order_id) {
        try{
            Order order = orderMapper.selectOrderById(order_id);
            //如果是票，更新票的状态
            String[] ticketIds = order.getDescription().split(",");
            //判断是否在退票禁止时间之后
            int mins = refundMapper.selectRefundStrategy().getTime();
            long current = System.currentTimeMillis();
            current += mins*60*1000;
            Date date = new Date(current);
            int scheduleId = ticketMapper.selectTicketById(Integer.parseInt(ticketIds[0])).getScheduleId();
            Date startTime = scheduleMapper.selectScheduleById(scheduleId).getStartTime();
            if(date.after(startTime)){
                return ResponseVO.buildFailure("开场前"+mins+"分钟内不能退票");
            }
            orderMapper.updateEndTime(order_id,new Timestamp(System.currentTimeMillis()));
            //更新订单状态
            orderMapper.updateOrderState(order_id,2);
            for(int i=0;i<ticketIds.length;i++){
                Ticket ticket = ticketMapper.selectTicketById(Integer.parseInt(ticketIds[i]));
                ticketMapper.updateTicketState(ticket.getId(),2);
            }
            //如果使用的vip卡，还钱
            if(order.getPayMethod()==1){
                int userId = order.getUser_id();
                VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
                VIPCardKind cardKind = vipCardMapper.selectVIPCardKindById(vipCard.getType());
                double amountWithVIPDiscount = cardKind.payByCardCalculate(order.getTotal());
                RefundStrategyForm refundStrategy = refundMapper.selectRefundStrategy();
                //根据退票测略，折算后的价钱
                double refundAmount = refundStrategy.getRefundAmount(amountWithVIPDiscount);
                double orginBalance = vipCard.getBalance();
                vipCardMapper.updateCardBalance(vipCard.getId(),orginBalance+refundAmount);
            }

            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("refundTickets failed");
        }
    }
}
