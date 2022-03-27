package com.ssonsh.refactoringstudy._05_global_data._17_encapsulate_variable;

public class Thermostats {

    private static Integer targetTemperature = 70;
    private static Boolean heating = true;
    private static Boolean cooling = false;
    private static Boolean readInFahrenheit = true;

    public static Integer getTargetTemperature() {
        return targetTemperature;
    }

    public static void setTargetTemperature(Integer targetTemperature) {
        // TODO. targetTemperature 값은 -10 ~ 50 사이 값이어야 한다.
        // TODO. targetTemperature 값이 설정 가능한 범위를 벗어난다면? Notification

        Thermostats.targetTemperature = targetTemperature;
    }

    public static Boolean getHeating() {
        return heating;
    }

    public static void setHeating(Boolean heating) {
        Thermostats.heating = heating;
    }

    public static Boolean getCooling() {
        return cooling;
    }

    public static void setCooling(Boolean cooling) {
        Thermostats.cooling = cooling;
    }

    public static Boolean getReadInFahrenheit() {
        return readInFahrenheit;
    }

    public static void setReadInFahrenheit(Boolean readInFahrenheit) {
        Thermostats.readInFahrenheit = readInFahrenheit;
    }
}
