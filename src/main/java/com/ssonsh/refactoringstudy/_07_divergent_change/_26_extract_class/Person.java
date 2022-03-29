package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class Person {

    private String name;

    private TelephoneNumber officeTelephoneNumber;

    public Person(String name, TelephoneNumber officeTelephoneNumber) {
        this.name = name;
        this.officeTelephoneNumber = officeTelephoneNumber;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String telephoneNumber(){
        return this.officeTelephoneNumber.telephoneNumber();
    }
}
