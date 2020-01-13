package com.example.cinema.controller.management;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**影厅管理
 * @author fjj
 * @date 2019/4/12 1:59 PM
 */
@RestController
public class HallController {
    @Autowired
    private HallService hallService;

    @RequestMapping(value = "/hall/all", method = RequestMethod.GET)
    public ResponseVO searchAllHall(){
        return hallService.searchAllHall();
    }

    //todo:添加影厅
    @RequestMapping(value = "/hall/add",method = RequestMethod.POST)
    public ResponseVO addHall(@RequestBody HallVO hallVO){
        return hallService.addHall(hallVO);
    }

    //todo:修改影厅信息
    @RequestMapping(value = "/hall/update",method = RequestMethod.POST)
    public ResponseVO updateHall(@RequestBody HallVO hallVO){
        return hallService.updateHall(hallVO);
    }

    //todo:删除影院信息
    @RequestMapping(value = "/hall/delete",method = RequestMethod.DELETE)
    public ResponseVO deleteHall(@RequestBody int hallId){
        return hallService.deleteHall(hallId);
    }
}
