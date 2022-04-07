package com.ssonsh.refactoringstudy._38_remove_middle_man;

import com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man.Department;
import com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void getManager() {
        Person nick = new Person("nick", null);
        Person keesun = new Person("keesun", new Department(nick));
        assertEquals(nick, keesun.getDepartment().getManager());
    }

}