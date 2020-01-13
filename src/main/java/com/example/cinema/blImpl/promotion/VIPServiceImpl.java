package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.bl.sales.OrderService;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.OrderMapper;
import com.example.cinema.po.Order;
import com.example.cinema.po.VIPCardKind;
import com.example.cinema.vo.*;
import com.example.cinema.po.VIPCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liying on 2019/4/14.
 */
@Service
public class VIPServiceImpl implements VIPService {
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    OrderMapper orderMapper;


    @Override
    public ResponseVO addVIPCardForm(VIPCardKindVO vipCardKindVO){
        try {
            vipCardMapper.insertOneVIPCardKind(vipCardKindVO);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("新增会员卡种类失败");
        }

    }

    @Override
    public ResponseVO updateVIPCardForm(VIPCardKindVO vipCardKindVO) {
        try{
            vipCardMapper.updateOneVIPCardKind(vipCardKindVO);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("更新会员卡优惠策略失败");
        }
    }

    @Override
    public ResponseVO getAllKindsOfVIPCard() {
        try{
            List<VIPCardKindVO> vipCardKindVOList = VIPCardKindPO2VO(vipCardMapper.selectAllKindsOfVIPCard());
            return ResponseVO.buildSuccess(vipCardKindVOList);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("获得全部会员卡失败");
        }

    }

    @Override
    public ResponseVO getVIPCardKindInfo(int cardKind) {
        try{
            VIPCardKind vipCardKind = vipCardMapper.selectVIPCardKindById(cardKind);
            VIPCardKindVO vipCardKindVO = new VIPCardKindVO(vipCardKind);
            return ResponseVO.buildSuccess(vipCardKindVO);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("获得该种会员卡失败");
        }
    }

    //todo
    @Override
    public ResponseVO getAllMemberInfo() {
        try{
            List<VIPWithTotal> vipWithTotals = new ArrayList<>();

            //先获得所有的会员ID
            List<VIPCard> vipCards = vipCardMapper.selectAllVIP();
            //    List<Integer> memberIds = new ArrayList<>();
            for(int i=0;i<vipCards.size();i++){
                //      memberIds.add(vipCards.get(i).getUserId());
                int userId = vipCards.get(i).getUserId();
                VIPCard vipCard0 = vipCardMapper.selectCardByUserId(userId);
                Timestamp joinTime = vipCard0.getJoinDate();
                List<Order> orders =  orderMapper.selectOrdersByUser(userId);
                double totals = 0.0;
                for(int j=0;j<orders.size();j++){
                    totals+=orders.get(j).getTotal();
                }
                VIPWithTotal vipWithTotal = new VIPWithTotal(userId,joinTime,totals);
                vipWithTotals.add(vipWithTotal);
            }
            return ResponseVO.buildSuccess(vipWithTotals);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("获得所有会员信息失败");
        }

    }

    private List<VIPCardKindVO> VIPCardKindPO2VO(List<VIPCardKind> poList) {
        List<VIPCardKindVO> voList = new ArrayList<>();
        for (int i = 0; i < poList.size(); i++) {
            VIPCardKindVO vo = new VIPCardKindVO(poList.get(i));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public ResponseVO addVIPCard(int userId,int cardKind) {
        VIPCard vipCard = new VIPCard();
        vipCard.setUserId(userId);
        vipCard.setBalance(0);
        vipCard.setType(cardKind);
        try {
            int id = vipCardMapper.insertOneCard(vipCard);
            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("addVIPCard failed");
        }
    }

    @Override
    public ResponseVO getCardById(int id) {
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("getCardById failed");
        }
    }
/*
    @Override
    public ResponseVO getVIPInfo() {
        VIPInfoVO vipInfoVO = new VIPInfoVO();
      //  vipInfoVO.setDescription(VIPCard.description);
        vipInfoVO.setPrice(VIPCard.price);
        return ResponseVO.buildSuccess(vipInfoVO);
    }
*/
    @Override
    public ResponseVO charge(VIPCardForm vipCardForm) {

        VIPCard vipCard = vipCardMapper.selectCardById(vipCardForm.getVipId());
        if (vipCard == null) {
            return ResponseVO.buildFailure("会员卡不存在");
        }
        int cardType = vipCard.getType();
        VIPCardKind vipCardKind = vipCardMapper.selectVIPCardKindById(cardType);
        double balance = vipCardKind.chargeCalculate(vipCardForm.getAmount());
        vipCard.setBalance(vipCard.getBalance() + balance);
        try {
            vipCardMapper.updateCardBalance(vipCardForm.getVipId(), vipCard.getBalance());
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("charge failed");
        }
    }

    @Override
    public ResponseVO getCardByUserId(int userId) {
        try {
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            if(vipCard==null){
                return ResponseVO.buildFailure("用户卡不存在");
            }
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("getCardByUserId failed");
        }
    }


}
