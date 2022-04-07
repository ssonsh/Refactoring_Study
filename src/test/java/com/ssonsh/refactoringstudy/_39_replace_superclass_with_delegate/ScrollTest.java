package com.ssonsh.refactoringstudy._39_replace_superclass_with_delegate;

import com.ssonsh.refactoringstudy._18_middle_man._39_replace_superclass_with_delegate.Scroll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrollTest {

    @Test
    void daysSinceLastCleaning() {
        Scroll scroll = new Scroll(1, "whiteship", null, LocalDate.of(2022, 1, 10));
        assertEquals(5, scroll.daysSinceLastCleaning(LocalDate.of(2022, 1, 15)));
    }

}