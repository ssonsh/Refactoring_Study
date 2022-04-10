package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Booking {

    protected Show show;

    protected LocalDateTime time;
    private PremiumDelegate premiumDelegate;

    public static Booking createBooking(Show show, LocalDateTime time){
        return new Booking(show, time);
    }

    public static Booking createPremiumBooking(Show show, LocalDateTime time, PremiumExtra extra){
        // PremiumBooking booking = new PremiumBooking(show, time, extra);
        Booking booking = createBooking(show, time);
        booking.premiumDelegate = new PremiumDelegate(booking, extra);
        return booking;
    }

    public Booking(Show show, LocalDateTime time) {
        this.show = show;
        this.time = time;
    }

    public boolean hasTalkback() {
        return (this.premiumDelegate != null) ?
               this.premiumDelegate.hasTalkback() :
               this.show.hasOwnProperty("talkback") && !this.isPeakDay();
    }

    protected boolean isPeakDay() {
        DayOfWeek dayOfWeek = this.time.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public double basePrice() {
        double result = this.show.getPrice();
        if (this.isPeakDay()) result += Math.round(result * 0.15);
        return (this.premiumDelegate != null) ? this.premiumDelegate.extendBasePrice(result) : result;
    }

    public boolean hasDinner(){
        return this.premiumDelegate != null && this.premiumDelegate.hasDinner();
    }
}
