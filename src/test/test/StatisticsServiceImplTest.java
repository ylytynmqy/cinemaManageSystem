package test;

import com.example.cinema.CinemaApplication;
import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.vo.MovieTotalBoxOfficeVO;
import com.example.cinema.vo.ResponseVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CinemaApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsServiceImplTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StatisticsService statisticsService;

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
    public void getTotalBoxOffice() {
        System.out.println("---------测试获得票房--------");
        ResponseVO vo = statisticsService.getTotalBoxOffice();
        if(vo.getSuccess()) {
            for (MovieTotalBoxOfficeVO movieTotalBoxOfficeVO : (List<MovieTotalBoxOfficeVO>) (vo.getContent())) {
                System.out.println(
                        "\nmovie id:" + movieTotalBoxOfficeVO.getMovieId()
                                + "\nname:" + movieTotalBoxOfficeVO.getName()
                                + "\nboxOffice:" + movieTotalBoxOfficeVO.getBoxOffice()
                );
            }
        }else{
            System.out.println("-----------fail-----------");
        }
    }


}
