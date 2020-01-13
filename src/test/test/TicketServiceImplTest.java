package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.po.Ticket;
import com.example.cinema.vo.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TicketServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TicketService ticketService;

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
    public void getRefundStrategy() {
//        ResponseEntity<String> response = this.restTemplate.getForEntity(this.base.toString() + "/test", String.class, "");
//        System.out.println(String.format("测试结果为：%s", response.getBody()));
        System.out.println("------测试获得退票策略------");
        RefundStrategyForm vo = (RefundStrategyForm) ticketService.getRefundStrategy().getContent();
        Assert.assertSame("获得退票策略有误",30,vo.getTime());


    }

    @Test
    @Transactional
    public void addTicket() {
        System.out.println("------测试添加票------");
        TicketForm ticketForm = new TicketForm();
        SeatForm seatForm = new SeatForm();
        seatForm.setColumnIndex(1);
        seatForm.setRowIndex(1);
        List<SeatForm> seatForms = new ArrayList<>();
        seatForms.add(seatForm);
        ticketForm.setScheduleId(3);
        ticketForm.setSeats(seatForms);
        ticketForm.setUserId(2);
        ResponseVO vo = ticketService.addTicket(ticketForm);
        if(vo.getSuccess()){
            System.out.println("---------pass----------");
        }else{
            System.out.println("---------fail---------");
        }
    }


    @Test
    public void getTicketByUser() {
        System.out.println("-------test 1--------");
        ResponseVO vo = ticketService.getTicketByUser(2);
        if(vo.getSuccess()){
            for(Ticket ticket:(List<Ticket>)vo.getContent()){
                System.out.println(
                        "id:"+ticket.getId()
                        +"\ncolumn:"+ticket.getColumnIndex()
                        +"\nrow:"+ticket.getRowIndex()
                        +"\ntime:"+ticket.getTime()
                        +"\nschedule id:"+ticket.getScheduleId()
                        +"\nstate:"+ticket.getState()
                );
            }
        }else{
            System.out.println("--------fail-------");
        }
    }
}
