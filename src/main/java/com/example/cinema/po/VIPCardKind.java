package com.example.cinema.po;

import com.example.cinema.vo.VIPCardForm;

import java.sql.Timestamp;
import java.util.List;

/*wfy 2019/5/29.*/
public class VIPCardKind {
    private int id;
    /**
     * VIPCard名称
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public VIPCardKind(){

    }

    /**
     * 计算充值金额折算
     * @param originAmount 初始的充值金额
     * @return
     */
    public double chargeCalculate(double originAmount){
        return (originAmount/this.targetAmount)*this.bonusAmount+originAmount;
    }

    /**
     * 计算买票折扣价
     * @param originAmount 原价
     * @return
     */
    public double payByCardCalculate(double originAmount){
        return originAmount*this.percent;
    }


}
