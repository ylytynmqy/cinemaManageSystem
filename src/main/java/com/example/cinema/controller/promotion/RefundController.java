package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.RefundService;
import com.example.cinema.vo.RefundStrategyForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class RefundController {
    @Autowired
    RefundService refundService;

    //修改退票策略
    @RequestMapping(value = "/refundStrategy/set",method = RequestMethod.POST)
    public ResponseVO setRefundStrategy(@RequestBody RefundStrategyForm refundStrategyForm){
        return refundService.setRefundStrategy(refundStrategyForm);
    }}
