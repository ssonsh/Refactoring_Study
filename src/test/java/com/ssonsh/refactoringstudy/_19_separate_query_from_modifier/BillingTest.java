package com.ssonsh.refactoringstudy._19_separate_query_from_modifier;

import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.Billing;
import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.Customer;
import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.EmailGateway;
import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


class BillingTest {

    @Test
    void totalOutstanding() {
        Billing billing = new Billing(new Customer("keesun", List.of(new Invoice(20), new Invoice(30))),
                                      new EmailGateway());

        double totalOutstanding = billing.totalOutstanding();

        Assertions.assertEquals(50d, billing.totalOutstanding());
    }
}