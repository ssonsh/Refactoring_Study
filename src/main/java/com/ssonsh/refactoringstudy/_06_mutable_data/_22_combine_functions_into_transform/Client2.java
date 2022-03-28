package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client2 {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        this.base = new ReadingClient().calculateBaseCharge(reading);
        this.taxableCharge = Math.max(0, this.base - taxThreshold(reading.year()));
    }

    private double taxThreshold(Year year) {
        return 5;
    }
}
