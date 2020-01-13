package com.example.cinema.bl.promotion;

import com.example.cinema.vo.MessageForm;
import com.example.cinema.vo.ResponseVO;
import org.apache.ibatis.annotations.Mapper;

public interface MessageService {
    //插入
    ResponseVO addOneMessage(MessageForm messageForm);

    //获得所有
    ResponseVO getAllMessages();

    //更新状态为已读
    ResponseVO updateRead();
}