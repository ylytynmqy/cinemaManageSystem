package com.example.cinema.vo;

import java.sql.Timestamp;

public class VIPWithTotal {
    private int userId;
    private Timestamp joinTime;
    private double total;

    public VIPWithTotal(){

    }
    public VIPWithTotal(int userId,Timestamp joinTime,double total){
        this.joinTime = joinTime;
        this.total = total;
        this.userId = userId;
    }

    public Timestamp getJoinTime() {
        return joinTime;
    }

    public double getTotal() {
        return total;
    }

    public int getUserId() {
        return userId;
    }

    public void setJoinTime(Timestamp joinTime) {
        this.joinTime = joinTime;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
