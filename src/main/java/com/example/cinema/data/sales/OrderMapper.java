package com.example.cinema.data.sales;

import com.example.cinema.po.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface OrderMapper {
    //添加一条订单a
    int insertOrder(Order order);
    //更新订单状态
    void updateOrderState(@Param("id") int id, @Param("state") int state);
    //获得所有的订单
    List<Order> selectOrdersByUser(int user_id);
    //根据order_id获得订单
    Order selectOrderById(int id);

    void updateEndTime(@Param("id")int id,@Param("endTime") Timestamp endTime);

    void updatePayMethod(@Param("id") int id,@Param("payMethod") int payMethod);

    // @Scheduled(cron = "0/1 * * * * ?")
    // void cleanExpiredOrder();
}
