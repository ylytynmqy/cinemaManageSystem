package com.example.cinema.bl.promotion;

import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPCardKindVO;


/**
 * Created by liying on 2019/4/14.
 */

public interface VIPService {

    ResponseVO addVIPCard(int userId,int cardKind);

    ResponseVO getCardById(int id);

 //   ResponseVO getVIPInfo();


    ResponseVO charge(VIPCardForm vipCardForm);

    ResponseVO getCardByUserId(int userId);

    //增加会员卡种类
    ResponseVO addVIPCardForm(VIPCardKindVO vipCardKindVO);
    //修改会员卡策略
    ResponseVO updateVIPCardForm(VIPCardKindVO vipCardKindVO);
    //获得所有种类的会员卡
    ResponseVO getAllKindsOfVIPCard();
    //根据卡的种类获得卡的信息
    ResponseVO getVIPCardKindInfo(int cardKind);

    //获得所有会员的信息（含累计消费金额）
    ResponseVO getAllMemberInfo();
}
