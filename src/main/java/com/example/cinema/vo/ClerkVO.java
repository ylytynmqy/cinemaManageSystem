package com.example.cinema.vo;

import com.example.cinema.po.Clerk;

public class ClerkVO {
    private Integer id;
    private String username;

    public ClerkVO(Clerk clerk){
        this.id = clerk.getId();
        this.username = clerk.getUsername();
    }
    public ClerkVO(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
