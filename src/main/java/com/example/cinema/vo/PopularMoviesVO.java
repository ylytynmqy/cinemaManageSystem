package com.example.cinema.vo;

import com.example.cinema.po.Movie;

import java.util.List;

public class PopularMoviesVO {
    private double boxoffice;
    private String movieName;


    public String getMovieName(){return movieName;}

    public void setMovieName(String movieName){this.movieName=movieName;}

    public double getBoxoffice(){return boxoffice;}

    public  void setBoxoffice(double boxoffice){this.boxoffice=boxoffice;}

}
