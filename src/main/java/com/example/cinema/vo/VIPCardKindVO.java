package com.example.cinema.vo;

import com.example.cinema.po.User;
import com.example.cinema.po.VIPCardKind;

import java.sql.Timestamp;
import java.util.List;

public class VIPCardKindVO {

    private int id;

    /**
     * VIPCard描述
     */
    private String description;
    /**
     * VIPCard价格
     */
    private int price;
    /**
     * VIPCard充值金额
     */
    private int targetAmount;
    /**
     * VIPCard赠送金额
     */
    private int bonusAmount;

    private double percent;

    public VIPCardKindVO(){

    }
    public VIPCardKindVO(VIPCardKind vipCardKind){
        this.bonusAmount=vipCardKind.getBonusAmount();
        this.description=vipCardKind.getDescription();
        this.price=vipCardKind.getPrice();
        this.targetAmount=vipCardKind.getTargetAmount();
        this.id=vipCardKind.getId();
        this.percent=vipCardKind.getPercent();
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getId() {
        return id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public  int getPrice(){return price;}

    public  void setPrice(int price){this.price=price;}

}
