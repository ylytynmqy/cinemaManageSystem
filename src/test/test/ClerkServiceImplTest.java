package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.management.ClerkService;
import com.example.cinema.po.Clerk;
import com.example.cinema.vo.ResponseVO;

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

public class ClerkServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ClerkService clerkService;

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
    public void getClerk() {
        System.out.println("test get clerks");
        ResponseVO vo = clerkService.getClerk();
        if(vo.getSuccess()){
            for(Clerk clerk:(List<Clerk>)(vo.getContent())){
                System.out.println(
                        "\nid:"+clerk.getId()
                        +"\nname:"+clerk.getUsername()
                );
            }
        }else{
            System.out.println("----------fail---------");
        }
    }

    @Test
    @Transactional
    public void deleteClerk() {
        System.out.println("test delete a clerk");
        ResponseVO vo = clerkService.deleteClerk(3);
        if(vo.getSuccess()){
            System.out.println("-------------pass-----------");
        }else{
            System.out.println("------------fail-----------");
        }
    }

}
