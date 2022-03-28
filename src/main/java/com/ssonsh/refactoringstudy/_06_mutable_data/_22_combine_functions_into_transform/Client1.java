package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client1 {

    double baseCharge;

    public Client1(Reading reading) {

        this.baseCharge = new ReadingClient().calculateBaseCharge(reading);
    }

}
