package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.management.HallService;
import com.example.cinema.blImpl.management.hall.HallServiceImpl;
import com.example.cinema.po.Hall;
import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class HallServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private HallService hallService;
    @Autowired
    private HallServiceImpl hallServiceImpl;

    @LocalServerPort
    private int port;
    private URL base;
    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }

    @Test
    public void searchAllHall() {
        ResponseVO vo = hallService.searchAllHall();
        if(vo.getSuccess()){
            for(HallVO hall:(List<HallVO>)(vo.getContent())){
                System.out.println(
                        "id:"+hall.getId()
                        +"\nname:"+hall.getName()
                        +"\ncolumn:"+hall.getColumn()
                        +"\nrow:"+hall.getRow()
                );
            }
        }else{
            System.out.println("---------fail---------");
        }
    }

    @Test
    @Transactional
    public void addHall() {
        HallVO hallVO = new HallVO();
        hallVO.setName("2号厅");
        hallVO.setColumn(8);
        hallVO.setRow(6);
        ResponseVO hall = hallService.addHall(hallVO);
        if(hall.getSuccess()){
            System.out.println("---------pass----------");
        }else{
            System.out.println("----------fail---------");
        }
    }

    @Test
    public void updateHall() {
    }

    @Test
    public void deleteHall() {
    }

    @Test
    public void checkSize() {
        HallVO hallVO = new HallVO();
        hallVO.setName("2号厅");
        hallVO.setColumn(17);
        hallVO.setRow(6);
        ResponseVO vo = hallServiceImpl.checkSize(hallVO);
        Assert.assertSame("check size fail",
                "超过影院规模最大限制",vo.getMessage());
    }

}
