package com.ssonsh.refactoringstudy._25_move_function;

import com.ssonsh.refactoringstudy._07_divergent_change._25_move_function.Account;
import com.ssonsh.refactoringstudy._07_divergent_change._25_move_function.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {

    @Test
    void bankCharge() {
        Account account = new Account(5, new AccountType(true));
        assertEquals(14.5, account.getBankCharge());

        account = new Account(8, new AccountType(true));
        assertEquals(15.35, account.getBankCharge());

        account = new Account(8, new AccountType(false));
        assertEquals(18.5, account.getBankCharge());
    }

}