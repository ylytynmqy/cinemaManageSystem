package com.example.cinema.blImpl.management.clerk;

import com.example.cinema.bl.management.ClerkService;
import com.example.cinema.data.management.ClerkMapper;
import com.example.cinema.po.Clerk;
import com.example.cinema.vo.ClerkRankVO;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VerificationCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClerkServiceImpl implements ClerkService {
    @Autowired
    ClerkMapper clerkMapper;

    @Override
    public ResponseVO getClerk(){
        try{
            List<Clerk> list=clerkMapper.selectAllClerk();
            return ResponseVO.buildSuccess(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getClerk Failed");
        }
    }

    @Override
    public ResponseVO deleteClerk(int clerkId){
        try{
            clerkMapper.deleteClerk(clerkId);
            clerkMapper.deleteClerkRank(clerkId);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("deleteClick Failed");
        }
    }
    @Override
    public ResponseVO addCode(VerificationCodeVO verificationCodeVO){
        try {
            clerkMapper.insertCode(verificationCodeVO.getCode());
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("addCode Failed");
        }
    }
    @Override
    public ResponseVO updateCode(VerificationCodeVO verificationCodeVO){
        try{
            clerkMapper.updateCode(verificationCodeVO.getCode());
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("updateCode Failed");
        }
    }

    @Override
    public ResponseVO getCode(){
        try {
            System.out.println("============="+clerkMapper.selectCode());

            return ResponseVO.buildSuccess(clerkMapper.selectCode());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getCode Failed");
        }
    }
    @Override
    public ResponseVO updateClerkRank(ClerkRankVO clerkRankVO){
        try{
            clerkMapper.updateClerkRank(clerkRankVO);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("updateClerkRank Failed");
        }
    }
    @Override
    public ResponseVO getClerkRank(int clerkId){
        try{ ;
            return ResponseVO.buildSuccess(clerkMapper.selectClerkRank(clerkId));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("getClerkRank Failed");
        }
    }

    @Override
    public ResponseVO setCode(int code) {
        try{
            return ResponseVO.buildSuccess(123);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(" Failed");
        }
    }
}
