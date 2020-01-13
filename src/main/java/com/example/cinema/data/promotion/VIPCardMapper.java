package com.example.cinema.data.promotion;

import com.example.cinema.po.VIPCard;
import com.example.cinema.po.VIPCardKind;
import com.example.cinema.vo.VIPCardKindVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/14.
 */
@Mapper
public interface VIPCardMapper {

    //增加一个会员
    int insertOneCard(VIPCard vipCard);

    VIPCard selectCardById(int id);

    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    VIPCard selectCardByUserId(int userId);

    //获得所有的会员卡
    List<VIPCard> selectAllVIP();

    //获得所有种类的会员卡
    List<VIPCardKind> selectAllKindsOfVIPCard();

    //增加一种会员卡
    int insertOneVIPCardKind(VIPCardKindVO vipCardKindVO);

    //修改一种会员卡的优惠策略
    int updateOneVIPCardKind(VIPCardKindVO vipCardKindVO);

    //根据卡的种类获得卡的信息
    VIPCardKind selectVIPCardKindById(int id);
}
