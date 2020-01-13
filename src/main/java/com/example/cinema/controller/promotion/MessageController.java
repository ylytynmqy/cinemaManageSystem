package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.MessageService;
import com.example.cinema.vo.MessageForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    //新添加一条message
    @PostMapping("/add")
    public ResponseVO addOneMessage(@RequestBody MessageForm messageForm){
        return messageService.addOneMessage(messageForm);
    }

    //获得所有的
    @GetMapping("/get/all")
    public ResponseVO getAllMessage(){
        return messageService.getAllMessages();
    }

    //更新消息的状态为已读
    @PostMapping("/set/read")
    public ResponseVO updateRead(){
        return messageService.updateRead();
    }
}
