package com.example.cinema.vo;

/**
 * 退票策略构造
 */
public class RefundStrategyForm {
    private int id;
    private int time;
    private double percent;

    public RefundStrategyForm(){

    }
    public int getId() {
        return id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getPercent() {
        return percent;
    }

    public int getTime() {
        return time;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 计算退款
     * @param originAmount 初始价格
     * @return
     */
    public double getRefundAmount(double originAmount){
        return originAmount*this.percent;
    }
}
