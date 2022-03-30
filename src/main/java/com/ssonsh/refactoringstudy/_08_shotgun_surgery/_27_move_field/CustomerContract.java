package com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field;

import java.time.LocalDate;

public class CustomerContract {

    private LocalDate startDate;

    private double discountRate;

    public CustomerContract(double discountRate, LocalDate startDate) {
        this.startDate = startDate;
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
