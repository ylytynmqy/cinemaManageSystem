package com.example.cinema.controller.management;

import com.example.cinema.bl.management.ClerkService;
import com.example.cinema.po.Clerk;
import com.example.cinema.vo.ClerkRankVO;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VerificationCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClerkController {
    @Autowired
    private ClerkService clerkService;

    @RequestMapping(value = "/clerk/get",method = RequestMethod.GET)
    public ResponseVO getClerk(){
        return clerkService.getClerk();
    }

    @RequestMapping(value = "clerk/delete/{clerkId}",method = RequestMethod.DELETE)
    public ResponseVO deleteClerk(@PathVariable int clerkId){
        return clerkService.deleteClerk(clerkId);
    }

    @RequestMapping(value = "/clerk/add/code",method = RequestMethod.POST)
    public ResponseVO addCode(@RequestBody VerificationCodeVO verificationCodeVO){ return clerkService.addCode(verificationCodeVO);}

    @RequestMapping(value = "/clerk/update/code",method = RequestMethod.POST)
    public ResponseVO updateCode(@RequestBody VerificationCodeVO verificationCodeVO){ return clerkService.updateCode(verificationCodeVO);}

//    @RequestMapping(value = "/clerk/get/code",method = RequestMethod.GET)
//    public ResponseVO getCode(){
//        return clerkService.getCode();
//    }

    @GetMapping("/clerk/get/code")
    public ResponseVO getCode(){
        return clerkService.getCode();
    }

    /**
     *
     * @param
     * @return
     */
    @PostMapping("/clerk/post/code")
    public ResponseVO setCode(int code){
        return clerkService.setCode(code);
    }

    @RequestMapping(value = "/clerk/update/rank",method = RequestMethod.POST)
    public ResponseVO updateRank(@RequestBody ClerkRankVO clerkRankVO){
        return clerkService.updateClerkRank(clerkRankVO);
    }
    @RequestMapping(value = "/clerk/get/rank/{clerkId}",method = RequestMethod.GET)
    public ResponseVO getRank(@PathVariable int clerkId){
        return clerkService.getClerkRank(clerkId);
    }
}
