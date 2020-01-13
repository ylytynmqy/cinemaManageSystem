package com.example.cinema.bl.management;

import com.example.cinema.vo.ClerkRankVO;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.ClerkVO;
import com.example.cinema.vo.VerificationCodeVO;

public interface ClerkService {
    /**
     *获得所有的员工
     * @return
     */
    ResponseVO getClerk();

    /**注销该员工的账号
     * @return
     */
    ResponseVO deleteClerk(int clerkId);

    /**
     * 新增验证码
     */
    ResponseVO addCode(VerificationCodeVO verificationCodeVO);

    /**
     * 更改验证码
     */
    ResponseVO updateCode(VerificationCodeVO verificationCodeVO);

    /**
     * 获取验证码
     * @return
     */
    ResponseVO getCode();

    /**
     * 更新用户等级
     */
    ResponseVO updateClerkRank(ClerkRankVO clerkRankVO);

    ResponseVO getClerkRank(int id);

    /**
     * sao
     */
    ResponseVO setCode(int code);
}
