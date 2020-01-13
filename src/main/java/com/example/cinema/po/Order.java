package com.example.cinema.po;

import com.example.cinema.vo.OrderForm;

import java.sql.Timestamp;

public class Order {
    /**
     * 订单id
     */
    private int id;
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
    private Timestamp end_time;
    //付款方式0银行卡1会员卡
    private int payMethod;

    public Order(){

    }
    public Order(OrderForm orderForm){
        setUser_id(orderForm.getUser_id());
        setDescription(orderForm.getDescription());
        setState(orderForm.getState());
        setType(orderForm.getType());
        setTotal(orderForm.getTotal());
        setJoin_time(orderForm.getJoin_time());
        setPayMethod(orderForm.getPayMethod());

    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getId(){
        return this.id;
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
    public Timestamp getJoin_time(){
        return this.join_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setId(int id){
        this.id=id;
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

    public void setJoin_time(Timestamp join_time) {
        this.join_time = join_time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }
}
