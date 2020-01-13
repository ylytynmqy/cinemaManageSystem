package com.example.cinema.vo;

public class VerificationCodeVO {
    private String code;
    VerificationCodeVO(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
