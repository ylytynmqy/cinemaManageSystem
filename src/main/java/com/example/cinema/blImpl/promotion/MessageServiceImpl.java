package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.MessageService;
import com.example.cinema.data.promotion.MessageMapper;
import com.example.cinema.po.Message;
import com.example.cinema.vo.MessageForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public ResponseVO addOneMessage(MessageForm messageForm) {
        try {
            messageMapper.insertOneMessage(messageForm);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("新增消息失败");
        }
    }

    @Override
    public ResponseVO getAllMessages() {
        try {
            List<Message> messages = messageMapper.selectAllMessages();
            return ResponseVO.buildSuccess(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("获取消息失败");
        }
    }

    @Override
    public ResponseVO updateRead() {
        try {
            messageMapper.updateRead();
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("获取消息失败");
        }
    }
}
