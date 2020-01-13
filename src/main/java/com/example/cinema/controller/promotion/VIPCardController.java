package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPCardKindVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liying on 2019/4/14.
 */
@RestController()
@RequestMapping("/vip")
public class VIPCardController {
    @Autowired
    VIPService vipService;

    @PostMapping("/add")
    public ResponseVO addVIP(@RequestParam int userId,int cardKind){
        return vipService.addVIPCard(userId,cardKind);
    }
    @GetMapping("{userId}/get")
    public ResponseVO getVIP(@PathVariable int userId){
        return vipService.getCardByUserId(userId);
    }
//
//    @GetMapping("/getVIPInfo")
//    public ResponseVO getVIPInfo(){
//        return vipService.getVIPInfo();
//    }

    @PostMapping("/charge")
    public ResponseVO charge(@RequestBody VIPCardForm vipCardForm){
        return vipService.charge(vipCardForm);
    }


    //发布新的会员卡
    @PostMapping("/card/publish")
    public ResponseVO publishVIPCard(@RequestBody VIPCardKindVO vipCardKindVO){
        return vipService.addVIPCardForm(vipCardKindVO);
    }
    //修改会员优惠策略
    @PostMapping("/card/update")
    public ResponseVO updateVIPCard(@RequestBody VIPCardKindVO vipCardKindVO){
        return vipService.updateVIPCardForm(vipCardKindVO);
    }
    //获得所有种类的会员卡
    @GetMapping("/card/all")
    public ResponseVO getAllKindsOfVIPCard(){
        return vipService.getAllKindsOfVIPCard();
    }
    //根据卡的种类获得卡的信息
    @GetMapping("/card/get/{cardType}")
    public ResponseVO getVIPCardKind(@PathVariable int cardType){
        return vipService.getVIPCardKindInfo(cardType);
    }

    //获得所有会员信息（包括累计金额）
    @GetMapping("/memberInfo")
    public ResponseVO getAllVIPMemberInfo(){
        return vipService.getAllMemberInfo();
    }





}
