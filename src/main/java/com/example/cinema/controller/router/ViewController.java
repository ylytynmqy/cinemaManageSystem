package com.example.cinema.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author deng
 * @date 2019/03/11
 */
@Controller
public class ViewController {
    @RequestMapping(value = "/index")
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = "/signUp")
    public String getSignUp() {
        return "signUp";
    }

    @RequestMapping(value = "/admin/cinema/manage")
    public String getAdminCinemaManage() {
        return "adminCinemaManage";
    }

    @RequestMapping(value = "/admin/promotion/manage")
    public String getAdminPromotionManage() {
        return "adminPromotionManage";
    }

    @RequestMapping(value="/admin/member/manage")
    public String getAdminMemberManage(){return "adminMemberManage";};

    @RequestMapping(value="/admin/clerk/manage")
    public String getAdminClerkManage(){return "adminClerkManage";};

    @RequestMapping(value = "/clerk/movie/manage")
    public String getClerkMovieManage() {
        return "clerkMovieManage";
    }

    @RequestMapping(value = "/clerk/movieDetail")
    public String getClerkMovieDetail(@RequestParam int id) { return "clerkMovieDetail"; }

    @RequestMapping(value = "/clerk/session/manage")
    public String getClerkSessionManage() {
        return "clerkScheduleManage";
    }

    @RequestMapping(value = "/clerk/cinema/statistic")
    public String getClerkCinemaStatistic() {
        return "clerkCinemaStatistic";
    }

    @RequestMapping(value = "/user/home")
    public String getUserHome() {
        return "userHome";
    }

    @RequestMapping(value = "/user/buy")
    public String getUserBuy() {
        return "userBuy";
    }

    @RequestMapping(value = "/user/order")
    public String getUserOrder(){return "userOrder";}

    @RequestMapping(value = "/user/movieDetail")
    public String getUserMovieDetail(@RequestParam int id) {
        return "userMovieDetail";
    }

    @RequestMapping(value = "/user/movieDetail/buy")
    public String getUserMovieBuy(@RequestParam int id) {
        return "userMovieBuy";
    }

    @RequestMapping(value = "/user/movie")
    public String getUserMovie() {
        return "userMovie";
    }

    @RequestMapping(value = "/user/member")
    public String getUserMember() {
        return "userMember";
    }

    @RequestMapping(value = "/user/message")
    public String getUserMessage() {
        return "userMessage";
    }
}
