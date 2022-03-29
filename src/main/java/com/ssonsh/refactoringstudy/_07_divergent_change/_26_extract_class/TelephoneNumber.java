package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class TelephoneNumber {
    private String areaCode;
    private String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String telephoneNumber() {
        return this.areaCode + " " + this.number;
    }

    public String getAreaCode() {
        return areaCode;
    }
    public String getNumber() {
        return number;
    }

}