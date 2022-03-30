package com.ssonsh.refactoringstudy._10_data_clumps;

public class Office {

    private String location;

    private TelephoneNumber officeTelephoneNumber;

    public Office(String location, TelephoneNumber officeTelephoneNumber) {
        this.location = location;
        this.officeTelephoneNumber = officeTelephoneNumber;
    }

    public String officeTelephoneNumber() {
        return this.officeTelephoneNumber.findNumber();
    }
}
