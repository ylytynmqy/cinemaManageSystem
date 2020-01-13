package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liying on 2019/4/16.
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @GetMapping("{userId}/get")
    public ResponseVO getCoupons(@PathVariable int userId){
        return couponService.getCouponsByUser(userId);
    }

    //获得未过期的优惠券
    @GetMapping("/get/unexpired")
    public ResponseVO getUnexpiredCoupons(){
        return couponService.getUnexpiredCoupons();
    }

    //
    @PostMapping("/send")
    public ResponseVO sendUserACoupon(@RequestParam int couponId,@RequestParam int userId){
        return couponService.issueCoupon(couponId,userId);
    }

}
