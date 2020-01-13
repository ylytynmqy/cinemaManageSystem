package com.example.cinema.vo;

import java.sql.Timestamp;

public class OrderForm {
    /**
     * 订单id
     */
    private int user_id;
    private double total;
    /**
     * 订单状态，0，1,2
     */
    private int state;
    /**
     * 订单种类，0买票，1买卡，2充卡
     */
    private int type;
    /**
     * 买票：票ID，买卡充卡空
     */
    private String description;
    /**
     * 确认订单的时间
     */
    private Timestamp join_time;
    //付款方式 0银行卡1会员卡
    private int payMethod;

    public OrderForm(){
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getUser_id(){
        return this.user_id;
    }
    public double getTotal(){
        return this.total;
    }
    public int getState(){
        return this.state;
    }
    public int getType(){
        return this.type;
    }
    public String getDescription(){
        return this.description;
    }

    public void setUser_id(int user_id){
        this.user_id=user_id;
    }
    public void setTotal(double total){
        this.total=total;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(int state) {
        this.state = state;
    }


    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getJoin_time() {
        return join_time;
    }

    public void setJoin_time(Timestamp join_time){
        this.join_time=join_time;
    }
}
