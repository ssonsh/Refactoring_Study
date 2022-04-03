package com.ssonsh.refactoringstudy._30_repliace_primitive_with_object;

import com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object.Order;
import com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object.OrderProcessor;
import com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object.Priority;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessorTest {

    @Test
    void numberOfHighPriorityOrders() {
        OrderProcessor orderProcessor = new OrderProcessor();
        long highPriorityOrders = orderProcessor.numberOfHighPriorityOrders(
                List.of(new Order(new Priority("low")),
                        new Order(new Priority("normal")),
                        new Order(new Priority("high")),
                        new Order(new Priority("rush"))));
        assertEquals(2, highPriorityOrders);
    }

}