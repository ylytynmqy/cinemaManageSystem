package com.example.cinema.vo;

import com.example.cinema.po.Hall;
import com.example.cinema.po.Ticket;

public class MoviePlacingConditionVO {
    private Integer movieId;
    private double placingRate;
    /*排片率*/
    private String name;



    public Integer getMovieId(){return movieId;}

    public void setMovieId(Integer movieId){this.movieId=movieId;}

    public double getPlacingRate(){return placingRate;}

    public void setPlacingRate(double placingRate){this.placingRate=placingRate;}

    public String getName(){return name;}

    public void setName(String name){this.name=name;}


}
