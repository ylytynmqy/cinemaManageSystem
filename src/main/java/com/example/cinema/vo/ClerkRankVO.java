package com.example.cinema.vo;

public class ClerkRankVO {
    private int clerkId;
    private int rank;

    public void setClerkId(int clerkId) {
        clerkId = clerkId;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getClerkId() {
        return clerkId;
    }

    public int getRank() {
        return rank;
    }

    ClerkRankVO(int clerkId,int rank){
        this.clerkId=clerkId;
        this.rank=rank;
    }
}
