package com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier;

import java.util.List;

public class Criminal {

    public void alertForMiscreant(List<Person> people) {
        // findMiscreant 메소드의 반환 값이 Blank인 경우 알람을 끄지 않아도 된다.
        if(!findMiscreant(people).isBlank()){
            setOffAlarms();
        }
    }

    public String findMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                return "Don";
            }

            if (p.getName().equals("John")) {
                return "John";
            }
        }

        return "";
    }

    private void setOffAlarms() {
        System.out.println("set off alarm");
    }
}
