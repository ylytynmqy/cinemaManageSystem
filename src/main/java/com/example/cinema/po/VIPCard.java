package com.example.cinema.po;


import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.vo.VIPCardKindVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

/**
 * Created by liying on 2019/4/14.
 * Changed by gyx on 2019/6/6
 */

public class VIPCard {
    @Autowired
    VIPCardMapper vipCardMapper;

   // private double price = 25;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 会员卡id
     */
    private int id;

    /**
     * 会员卡余额
     */
    private double balance;

    /**
     * 办卡日期
     */
    private Timestamp joinDate;

    /**
     * 会员卡种类
     */
    private int type;



    public VIPCard() {

    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }
/*
    public double getPrice() {
        return price;
    }

    /*根据种类获得卡的信息*/
    private VIPCardKind getCardKind(){
        return vipCardMapper.selectVIPCardKindById(this.type);
      //  return null;
    }

    /**
     * 计算实际获得的金额
     * @param amount 充值金额
     * @return

    public double calculate(double amount) {
        VIPCardKind vipCardKind = getCardKind();
        this.price = vipCardKind.getPrice();
        int targetAmount = vipCardKind.getTargetAmount();
        int bonusAmount = vipCardKind.getBonusAmount();
        return (int)(amount/targetAmount)*bonusAmount+amount;

    }
    */
}
