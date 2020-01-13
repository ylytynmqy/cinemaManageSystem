package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.promotion.VIPService;

import com.example.cinema.po.VIPCard;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPCardKindVO;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class VIPServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private VIPService vipService;

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
    @Transactional
    public void addVIPCardForm() {
        VIPCardKindVO vipCardKindVO = new VIPCardKindVO();
        vipCardKindVO.setBonusAmount(20);
        vipCardKindVO.setTargetAmount(200);
        vipCardKindVO.setDescription("金卡");
        vipCardKindVO.setPercent(0.99);
        vipCardKindVO.setPrice(70);
        ResponseVO vo = vipService.addVIPCardForm(vipCardKindVO);
        if(vo.getSuccess()){
            System.out.println("-----------pass---------");
        }else{
            System.out.println("-----------fail----------");
        }
    }

    @Test
    @Transactional
    public void addVIPCard() {
        VIPCard vipCard = new VIPCard();
        vipCard.setType(1);
        vipCard.setUserId(2);
        vipCard.setBalance(0);
        ResponseVO vo = vipService.addVIPCard(2,1);
        if(vo.getSuccess()){
            System.out.println("-----------pass---------");
        }else{
            System.out.println("-----------fail----------");
        }
    }
}
