package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client3 {

    private double basicChargeAmount;

    public Client3(Reading reading) {
        this.basicChargeAmount = new ReadingClient().enrichReading(reading).baseCharge();
    }
}
