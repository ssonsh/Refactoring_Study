package com.ssonsh.refactoringstudy._10_data_clumps;

public class TelephoneNumber {

    private String areaCode;
    private String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String findNumber(){
        return this.areaCode + "-" + this.number;
    }
}
