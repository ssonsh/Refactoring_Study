package com.ssonsh.refactoringstudy._06_mutable_data._23_change_reference_to_value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordTelephoneNumberTest {

    @Test
    void recordTest(){
        RecordTelephoneNumber number1 = new RecordTelephoneNumber("123", "1234");
        RecordTelephoneNumber number2 = new RecordTelephoneNumber("123", "1234");

        assertEquals(number1, number2);
    }
}