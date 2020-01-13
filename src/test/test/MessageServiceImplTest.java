package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.promotion.MessageService;

import com.example.cinema.po.Message;
import com.example.cinema.vo.MessageForm;
import com.example.cinema.vo.ResponseVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.ResponseCache;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class MessageServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MessageService messageService;

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
    public void addOneMessage() {
        MessageForm messageForm = new MessageForm();
        messageForm.setDescription(
                "somethingsomethingsomething"
        );
        ResponseVO vo = messageService.addOneMessage(messageForm);
        if(vo.getSuccess()){
            System.out.println("---------pass----------");
        }else{
            System.out.println("---------fail---------");
        }
    }
}
