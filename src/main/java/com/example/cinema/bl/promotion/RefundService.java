package com.example.cinema.bl.promotion;

import com.example.cinema.vo.RefundStrategyForm;
import com.example.cinema.vo.ResponseVO;

public interface RefundService {
    /**
     * 设置退票策略
     * @param refundStrategyForm
     * @return
     */
    ResponseVO setRefundStrategy(RefundStrategyForm refundStrategyForm);
}

