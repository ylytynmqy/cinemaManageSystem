package com.example.cinema.data.management;

import com.example.cinema.po.Clerk;
import com.example.cinema.vo.ClerkRankVO;
import com.example.cinema.vo.VerificationCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClerkMapper {
    /**
     * 返回所有员工
     * @return
     */
    List<Clerk> selectAllClerk();

    /**
     * 删除员工
     */
    void deleteClerk(int clerkId);

    /**
     * 新增验证码
     */
    void insertCode(String code);

    /**
     * 更新验证码
     */
    void updateCode(String code);

    /**
     * 选择验证码
     * @return
     */
    String selectCode();

    /**
    * 新员工增设等级
    */
    void insertClerkRank(@Param("clerkId")int clerkId, @Param("rank")int rank);
    /**
     * 更新用户等级
     */
    void updateClerkRank(ClerkRankVO clerkRankVO);

    void deleteClerkRank(int clerkId);

    int selectClerkRank(int clerkId);
}
