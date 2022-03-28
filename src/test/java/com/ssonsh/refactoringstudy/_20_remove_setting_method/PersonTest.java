package com.ssonsh.refactoringstudy._20_remove_setting_method;

import com.ssonsh.refactoringstudy._06_mutable_data._20_remove_setting_method.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void person() {
        Person person = new Person(10);
        // person.setId(10);
        person.setName("keesun");
        assertEquals(10, person.getId());
        assertEquals("keesun", person.getName());
        person.setName("whiteship");
        assertEquals("whiteship", person.getName());
    }

}