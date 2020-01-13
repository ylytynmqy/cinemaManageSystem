package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.RefundMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 *
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    RefundMapper refundMapper;

    @Override
    public ResponseVO getRefundStrategy() {
        try{
            RefundStrategyForm refundStrategyForm = refundMapper.selectRefundStrategy();
            return ResponseVO.buildSuccess(refundStrategyForm);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        try {
            List<Integer> ticketIds = new ArrayList<>();
            int userId=ticketForm.getUserId();
            int scheduleId=ticketForm.getScheduleId();
            List<SeatForm> seats=ticketForm.getSeats();
            for (SeatForm seat:seats) {

                int columnIndex=seat.getColumnIndex();
                int rowIndex=seat.getRowIndex();

                Ticket ticket=new Ticket();

                ticket.setColumnIndex(columnIndex);
                ticket.setRowIndex(rowIndex);
                ticket.setUserId(userId);
                ticket.setScheduleId(scheduleId);
                ticket.setState(0);
                ticketMapper.insertTicket(ticket);
                ticketIds.add(ticketMapper.selectTicketByScheduleIdAndSeat(scheduleId,columnIndex,rowIndex).getId());
            }
            return ResponseVO.buildSuccess(ticketIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("addTicket failed");
        }
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
        try{
            for (int ticketId:id) {
                ticketMapper.deleteTicket(ticketId);
            }
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("cancelTicket failed");
        }
    }

    @Override
    @Transactional
    // TODO:完成购票【不使用会员卡】流程包括校验优惠券和根据优惠活动赠送优惠券
    public ResponseVO completeTicket(List<Integer> id, int couponId) {

        double amount=0.0;
        double amountWithCoupon=0.0;
        // 根据优惠券的id找到优惠券
        Coupon coupon;
        //根据票的id找到电影票,PO列表
        List<Ticket> tickets = new ArrayList<>();
        int ticketNum = id.size();
        //当前日期
        Date date = new Date();
        //当前时间戳
        Timestamp timestamp = new Timestamp(date.getTime());
        //票的vo列表
        List<TicketVO> ticketVOS = new ArrayList<>();
        //根据电影查找活动
        List<Activity> activities = new ArrayList<>();
        int userId=0;

        try{
            //TODO
            for(int i=0;i<ticketNum;i++){
                Ticket ticket = ticketMapper.selectTicketById(id.get(i));
                tickets.add(ticket);

                amount+=ticket.calculateTicketFare();
            }

            for(Ticket ticket:tickets){
                ticketMapper.updateTicketState(ticket.getId(),1);
            }
            //根据电影ID获得活动
            activities = tickets.get(0).selectActivitiesByMovie();
            userId=tickets.get(0).getUserId();

            //根据优惠活动赠送优惠券
            for (Activity activitiy:activities) {
                if(date.before(activitiy.getEndTime())&&date.after(activitiy.getStartTime())){
                    couponMapper.insertCouponUser(activitiy.getCoupon().getId(),userId);//可能会有重复
                }
            }

            if(couponId!=-1) {
                coupon=couponMapper.selectById(couponId);
                //优惠券使用条件：日期；金额
                if (date.after(coupon.getStartTime()) && date.before(coupon.getEndTime())) {
                    if (amount >= coupon.getTargetAmount()) {
                        //amountWithCoupon=amount-coupon.getDiscountAmount();
                        couponMapper.deleteCouponUser(couponId,userId);
                    }
                } else {
                    return ResponseVO.buildFailure("不在优惠券使用期限内");
                }
            }
            //待定
            TicketWithCouponVO ticketWithCouponVO = new TicketWithCouponVO();
            ticketWithCouponVO.setTicketVOList(ticketVOS);
            ticketWithCouponVO.setTotal(amount);
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("completeTicket failed");
        }
        //  return null;
    }

    @Override
    @Transactional
    //TODO:完成购票【使用会员卡】流程包括会员卡扣费、校验优惠券和根据优惠活动赠送优惠券
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
        double amount=0.0;
        double couponDiscount=0.0;
        // 根据优惠券的id找到优惠券
        Coupon coupon;
        //根据票的id找到电影票,PO列表
        List<Ticket> tickets = new ArrayList<>();
        int ticketNum = id.size();
        //当前日期
        Date date = new Date();
        //当前时间戳
        Timestamp timestamp = new Timestamp(date.getTime());
        //票的vo列表
        List<TicketVO> ticketVOS = new ArrayList<>();
        //根据电影查找活动
        List<Activity> activities = new ArrayList<>();
        int userId=0;

        try{
            for(int i=0;i<ticketNum;i++){
                Ticket ticket = ticketMapper.selectTicketById(id.get(i));
                tickets.add(ticket);

                //票的VO对象
                TicketVO ticketVO = ticket.getVO();
                ticketVOS.add(ticketVO);

                amount+=ticket.calculateTicketFare();
            }
            for(Ticket ticket:tickets){
                ticketMapper.updateTicketState(ticket.getId(),1);
            }
//            //根据电影ID获得活动
            activities = tickets.get(0).selectActivitiesByMovie();
            userId=tickets.get(0).getUserId();

            //根据优惠活动赠送优惠券
            for (Activity activitiy:activities) {
                if(date.before(activitiy.getEndTime())&&date.after(activitiy.getStartTime())){
                    couponMapper.insertCouponUser(activitiy.getCoupon().getId(),userId);//可能会有重复
                }
            }
            if(couponId!=-1) {
                coupon=couponMapper.selectById(couponId);
                //优惠券使用条件：日期；金额
                if (date.after(coupon.getStartTime()) && date.before(coupon.getEndTime())) {
                    // if (amount >= coupon.getTargetAmount()) {
                    couponDiscount=coupon.getDiscountAmount();
                    couponMapper.deleteCouponUser(couponId,userId);
                    // }
                } else {
                    return ResponseVO.buildFailure("不在优惠券使用期限内");
                }
            }
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            double originBalance = vipCard.getBalance();
            int cardKind = vipCard.getType();
            VIPCardKind vipCardKind = vipCardMapper.selectVIPCardKindById(cardKind);
            double percent = vipCardKind.getPercent();
            double amountWithVIPCoupon = amount*percent-couponDiscount;
            if(amountWithVIPCoupon<=originBalance){
                //卡里扣钱
                if(amountWithVIPCoupon>0) {
                    vipCardMapper.updateCardBalance(vipCard.getId(), originBalance - amountWithVIPCoupon);
                }
                //         else 不扣钱
            }
            else{
                return ResponseVO.buildFailure("余额不足");
            }
            //待定
            TicketWithCouponVO ticketWithCouponVO = new TicketWithCouponVO();
            ticketWithCouponVO.setTicketVOList(ticketVOS);
            ticketWithCouponVO.setTotal(amount);
            return ResponseVO.buildSuccess(ticketWithCouponVO);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("completeByVIPCard failed");
        }
    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("getBySchedule failed");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        try{
            List<Ticket> tickets = ticketMapper.selectTicketByUser(userId);
            return ResponseVO.buildSuccess(tickets);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getTicketByUser failed");
        }
    }
    @Override
    public ResponseVO getTicketByTicket(int ticketId){
        try{
            Ticket ticket = ticketMapper.selectTicketById(ticketId);
            return ResponseVO.buildSuccess(ticket);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getTicketByTicket Failed");
        }
    }




}
