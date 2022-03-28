package com.ssonsh.refactoringstudy._21_replace_derived_variable_with_query;

import com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query.ProductionPlan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductionPlanTest {

    @Test
    void production() {
        ProductionPlan productionPlan = new ProductionPlan();
        productionPlan.applyAdjustment(10);
        productionPlan.applyAdjustment(20);
        assertEquals(30, productionPlan.getProduction());
    }

}