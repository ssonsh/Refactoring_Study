package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class ReadingClient {
    protected double taxThreshold(Year year){
        return 5;
    }

    protected double baseRate(Month month, Year year){
        return 10;
    }

    /**
     * 변하지 않는 Reading reading 매개변수를
     * 또다른 변하지 않는 Immutable 한 EnrichReading 으로 반환한다.
     * @param reading
     * @return
     */
    protected EnrichReading enrichReading(Reading reading){
        return new EnrichReading(reading, calculateBaseCharge(reading));
    }


    public double calculateBaseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }
}
