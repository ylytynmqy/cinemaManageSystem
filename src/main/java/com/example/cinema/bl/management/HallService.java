package com.example.cinema.bl.management;

import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
public interface HallService {
    /**
     * 搜索所有影厅
     * @return
     */
    //s
    ResponseVO searchAllHall();

    /**
     * 增加影厅
     * @param hallVO
     * @return
     */
    ResponseVO addHall(HallVO hallVO);

    /**
     * 修改影厅信息
     * @param hallVO
     * @return
     */
    ResponseVO updateHall(HallVO hallVO);

    /**
     * 删除影厅信息
     * @param hallId
     * @return
     */
    ResponseVO deleteHall(int hallId);
}
