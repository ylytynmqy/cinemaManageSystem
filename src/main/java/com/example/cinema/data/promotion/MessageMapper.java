package com.example.cinema.data.promotion;

import com.example.cinema.po.Message;
import com.example.cinema.vo.MessageForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //添加一条消息
    int insertOneMessage(MessageForm messageForm);

    //获取全部的消息
    List<Message> selectAllMessages();

    //更改状态为已读
    void updateRead();
}
