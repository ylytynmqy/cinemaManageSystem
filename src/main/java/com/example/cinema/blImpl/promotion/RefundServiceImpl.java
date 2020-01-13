package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.RefundService;
import com.example.cinema.data.promotion.RefundMapper;
import com.example.cinema.vo.RefundStrategyForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RefundServiceImpl implements RefundService {
    @Autowired
    RefundMapper refundMapper;

    @Override
    public ResponseVO setRefundStrategy(RefundStrategyForm refundStrategyForm) {
        try {
            if(refundStrategyForm.getTime()<0){
                return ResponseVO.buildFailure("禁止退票时间不能为负");
            }
            if(refundStrategyForm.getPercent()>1){
                return ResponseVO.buildFailure("退票百分比不能大于1");
            }
            int num = refundMapper.selectRefundStrategyCount();
            if(num==0){
                refundMapper.insertRefundStrategy(refundStrategyForm);
            }
            else if(num==1){
                refundMapper.updateRefundStrategy(refundStrategyForm);
            }
            else{
                return ResponseVO.buildFailure("退票策略计数错误");
            }
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("setRefundStrategy failed");
        }
    }
}

