package com.ssonsh.refactoringstudy._37_hide_delegate;

import com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate.Department;
import com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void manager() {
        Person keesun = new Person("keesun");
        Person nick = new Person("nick");
        keesun.setDepartment(new Department("m365deploy", nick));

        Person manager = keesun.getManager();
        assertEquals(nick, manager);
    }

}