package com.ssonsh.refactoringstudy._21_replace_derived_variable_with_query;

import com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query.Discount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountTest {

    @Test
    void discount() {
        Discount discount = new Discount(100);
        assertEquals(100, discount.getDiscountedTotal());

        discount.setDiscount(10);
        assertEquals(90, discount.getDiscountedTotal());
    }

}