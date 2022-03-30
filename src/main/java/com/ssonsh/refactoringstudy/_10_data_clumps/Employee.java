package com.ssonsh.refactoringstudy._10_data_clumps;

public class Employee {

    private String name;
    private TelephoneNumber personalTelephoneNumber;

    public Employee(String name, TelephoneNumber personalTelephoneNumber) {
        this.name = name;
        this.personalTelephoneNumber = personalTelephoneNumber;
    }

    public String personalTelephoneNumber() {
        return personalTelephoneNumber.findNumber();
    }

    public String getName() {
        return name;
    }

}
