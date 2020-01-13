package com.example.cinema.data.promotion;

import com.example.cinema.vo.RefundStrategyForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefundMapper {
    //插入一条策略
    int insertRefundStrategy(RefundStrategyForm refundStrategyForm);
    //更新一条策略
    int updateRefundStrategy(RefundStrategyForm refundStrategyForm);
    //查询记录数，以便决定后续插入还是修改
    int selectRefundStrategyCount();
    //获得一条策略信息
    RefundStrategyForm selectRefundStrategy();
}
