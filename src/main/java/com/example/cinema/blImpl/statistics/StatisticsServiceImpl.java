package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getMoviePlacingRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));


            List<MoviePlacingConditionVO> moviePlacingConditionVOList=new ArrayList<>();

            List<Hall> hallList = hallMapper.selectAllHall();

            List<List<ScheduleItem>> scheduleItemList=new ArrayList<>();
            /*一个影厅所有电影的排片*/
            for(Hall hall: hallList) {
                scheduleItemList.add(scheduleMapper.selectSchedule( hall.getId(),date, getNumDayAfterDate(date, 1)));
            }



            Map<Integer,List<ScheduleItem>> moviemap=new HashMap<>();

            for(List<ScheduleItem> scheduleItemList1:scheduleItemList){
                for(ScheduleItem scheduleItem:scheduleItemList1){
                    if(moviemap.containsKey(scheduleItem.getMovieId())){
                        List<ScheduleItem> temp=moviemap.get(scheduleItem.getMovieId());
                        temp.add(scheduleItem);
                        moviemap.put(scheduleItem.getMovieId(),temp);
                    }
                    else{
                        List<ScheduleItem> movieScheduleItem=new ArrayList<>();
                        movieScheduleItem.add(scheduleItem);
                        moviemap.put(scheduleItem.getMovieId(),movieScheduleItem);
                    }

                }

            }
            /*不同电影的所有排片信息*/


            Map<Integer,List<List<Ticket>>> ticketmap=new HashMap<>();
            for(Integer key:moviemap.keySet()){
                for(ScheduleItem scheduleItem:moviemap.get(key)){
                    if(ticketmap.containsKey(scheduleItem.getMovieId())){
                        List<List<Ticket>> temp=ticketmap.get(scheduleItem.getMovieId());
                        temp.add(ticketMapper.selectTicketsBySchedule(scheduleItem.getId()));
                        ticketmap.put(scheduleItem.getMovieId(),temp);
                    }
                    else{
                        List<List<Ticket>> ticketScheduleItem=new ArrayList<>();
                        ticketScheduleItem.add(ticketMapper.selectTicketsBySchedule(scheduleItem.getId()));
                        ticketmap.put(scheduleItem.getMovieId(),ticketScheduleItem);

                    }
                }
            }


            for(Integer key:moviemap.keySet()){
                MoviePlacingConditionVO moviePlacingConditionVO=new MoviePlacingConditionVO();
                double scheduleSeatNum=0;
                double audienceNum=0;
                for(ScheduleItem scheduleItem:moviemap.get(key)){
                    Hall hall=hallMapper.selectHallById(scheduleItem.getHallId());
                    scheduleSeatNum+=hall.getRow()*hall.getColumn();
                }
                for(List<Ticket> ticketList:ticketmap.get(key)){
                    for(Ticket ticket:ticketList){
                        if(ticket.getState()==1){
                            audienceNum+=1;
                        }
                    }

                }
                Movie movie=movieMapper.selectMovieById(key);
                moviePlacingConditionVO.setName(movie.getName());

                moviePlacingConditionVO.setPlacingRate(Double.parseDouble(String.format("%.2f",audienceNum/scheduleSeatNum)));
                moviePlacingConditionVOList.add(moviePlacingConditionVO);
            }


            return ResponseVO.buildSuccess(moviePlacingConditionVOList);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
        //要求见接口说明

    }

    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        try {


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -days);


            List<Hall> hallList = hallMapper.selectAllHall();
            List<List<ScheduleItem>> scheduleItemList=new ArrayList<>();
            /*一个影厅所有电影的排片*/


            for(int i=1;i<=days;i++){
                Date date = getNumDayAfterDate(startDate, i);
                for(Hall hall: hallList) {
                    scheduleItemList.add(scheduleMapper.selectSchedule( hall.getId(),date, getNumDayAfterDate(date, 1)));
                }


            }


            Map<Integer,List<ScheduleItem>> moviemap=new HashMap<>();

            for(List<ScheduleItem> scheduleItemList1:scheduleItemList){
                for(ScheduleItem scheduleItem:scheduleItemList1){
                    if(moviemap.containsKey(scheduleItem.getMovieId())){
                        List<ScheduleItem> temp=moviemap.get(scheduleItem.getMovieId());
                        temp.add(scheduleItem);
                        moviemap.put(scheduleItem.getMovieId(),temp);
                    }
                    else{
                        List<ScheduleItem> movieScheduleItem=new ArrayList<>();
                        movieScheduleItem.add(scheduleItem);
                        moviemap.put(scheduleItem.getMovieId(),movieScheduleItem);
                    }

                }

            }



            Map<Integer,List<List<Ticket>>> ticketmap=new HashMap<>();
            for(Integer key:moviemap.keySet()){
                for(ScheduleItem scheduleItem:moviemap.get(key)){
                    if(ticketmap.containsKey(scheduleItem.getMovieId())){
                        List<List<Ticket>> temp=ticketmap.get(scheduleItem.getMovieId());
                        temp.add(ticketMapper.selectTicketsBySchedule(scheduleItem.getId()));
                        ticketmap.put(scheduleItem.getMovieId(),temp);
                    }
                    else{
                        List<List<Ticket>> ticketScheduleItem=new ArrayList<>();
                        ticketScheduleItem.add(ticketMapper.selectTicketsBySchedule(scheduleItem.getId()));
                        ticketmap.put(scheduleItem.getMovieId(),ticketScheduleItem);

                    }
                }
            }




            Map<Integer,Double> boxofficemap=new HashMap<>();
            for(Integer key:ticketmap.keySet()){
                double daysBoxOffice=0;
                for(List<Ticket> ticketList:ticketmap.get(key)){
                    for(Ticket ticket:ticketList){
                        if(ticket.getState()==1){
                            List<ScheduleItem> temp=moviemap.get(key);
                            daysBoxOffice+=temp.get(0).getFare();
                        }
                    }
                }
                boxofficemap.put(key,daysBoxOffice);
            }

            List<Map.Entry<Integer,Double>> list=new ArrayList<Map.Entry<Integer, Double>>(boxofficemap.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
                @Override
                public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            List<Movie> movies=new ArrayList<>();
            for(Map.Entry<Integer,Double> mapping:list){
                Movie movie=movieMapper.selectMovieById(mapping.getKey());
                movies.add(movie);
            }

            List<PopularMoviesVO> popularMoviesVOList=new ArrayList<>();
            if(movieNum<=movies.size()){
                for(int i=0;i<movieNum;i++){
                    PopularMoviesVO popularMoviesVO=new PopularMoviesVO();
                    popularMoviesVO.setBoxoffice(boxofficemap.get(movies.get(i).getId()));
                    popularMoviesVO.setMovieName(movies.get(i).getName());
                    popularMoviesVOList.add(popularMoviesVO);

                }
            }
            else{
                for(int i=0;i<movies.size();i++){
                    PopularMoviesVO popularMoviesVO=new PopularMoviesVO();
                    popularMoviesVO.setBoxoffice(boxofficemap.get(movies.get(i).getId()));
                    popularMoviesVO.setMovieName(movies.get(i).getName());
                    popularMoviesVOList.add(popularMoviesVO);
                }

            }



            return ResponseVO.buildSuccess(popularMoviesVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

        //要求见接口说明
    }


    /**
     * 获得num天后的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }



    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }








}
