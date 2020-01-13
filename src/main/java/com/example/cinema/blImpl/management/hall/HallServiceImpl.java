package com.example.cinema.blImpl.management.hall;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.bl.management.ScheduleService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.po.Hall;
import com.example.cinema.po.ScheduleItem;
import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
@Service
public class HallServiceImpl implements HallService, HallServiceForBl {
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    private static final int MAX_COLUMN=14;

    @Override
    public ResponseVO searchAllHall() {
        try {
            return ResponseVO.buildSuccess(hallList2HallVOList(hallMapper.selectAllHall()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("searchAllHall failed");
        }
    }

    @Override
    public Hall getHallById(int id) {
        try {
            return hallMapper.selectHallById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private List<HallVO> hallList2HallVOList(List<Hall> hallList){
        List<HallVO> hallVOList = new ArrayList<>();
        for(Hall hall : hallList){
            hallVOList.add(new HallVO(hall));
        }
        return hallVOList;
    }

    @Override
    //增加影厅(checkSize
    public ResponseVO addHall(HallVO hallVO){
        Hall hall = new Hall();
        hall.setName(hallVO.getName());
        hall.setColumn(hallVO.getColumn());
        hall.setRow(hallVO.getRow());
        try {
            ResponseVO responseVO=checkSize(hallVO);
            if(!responseVO.getSuccess()){  //大小不符合
                return responseVO;
            }
            hallMapper.insertHallInfo(hall);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("addHall failed");
        }
    }

    @Override
    //修改影厅信息(检查大小和排片
    public ResponseVO updateHall(HallVO hallVO){
        try{
            ResponseVO responseVO=checkSchedule(hallVO.getId());
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            ResponseVO responseVO1=checkSize(hallVO);
            if(!responseVO1.getSuccess()){
                return responseVO1;
            }
            hallMapper.updateHallInfo(hallVO);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("updateHall failed");
        }
    }

    @Override
    //删除影厅信息（前置检查schedule
    public ResponseVO deleteHall(int hallId){
        try{
            ResponseVO responseVO = checkSchedule(hallId);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            hallMapper.deleteHallInfo(hallId);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("deleteHall failed");
        }
    }

    //检查影院规模输入是否符合规则
    public ResponseVO checkSize(HallVO hallVO){
        //非负
        //不能过大
        int row=hallVO.getRow();
        int column=hallVO.getColumn();
        if(column>MAX_COLUMN){
            return ResponseVO.buildFailure("超过影院规模最大限制");
        }
        if(row<0||column<0){
            return ResponseVO.buildFailure("影厅行或列不能为负");
        }
        return ResponseVO.buildSuccess();
    }

    //删除和修改影厅之前检查是否影厅在观众可见时间内有排片
    public ResponseVO checkSchedule(int hallId){
        Date date = new Date();
        int scheduleViewDays = scheduleMapper.selectView();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,scheduleViewDays);
        Date endDate = c.getTime();

        List<ScheduleItem> scheduleItems = scheduleMapper.selectSchedule(hallId,date,endDate);
        if(scheduleItems.size()==0){
            return ResponseVO.buildSuccess();
        }
        else{
            return ResponseVO.buildFailure("当前影厅在观众可见排片时间内有排片，不可删除或修改");
        }
    }

}
