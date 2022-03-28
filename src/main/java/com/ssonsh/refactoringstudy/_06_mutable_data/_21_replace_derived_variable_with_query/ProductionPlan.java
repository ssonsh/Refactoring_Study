package com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlan {

    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
    }

    public double getProduction() {
        // adjustments.stream().reduce((double) 0, Double::sum);
        return adjustments.stream().mapToDouble(Double::valueOf).sum();
    }
}
