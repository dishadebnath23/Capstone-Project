package com.banking.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void shouldCreateAndReadAccount() {
        Account account = new Account();
        account.setId("a1");
        account.setAccountNumber("123456");
        account.setBalance(1000.0);

        assertEquals("a1", account.getId());
        assertEquals("123456", account.getAccountNumber());
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    void shouldCreateAndReadTransaction() {
        Transaction tx = new Transaction();
        tx.setId("t1");
        tx.setAmount(500.0);
        tx.setType("DEBIT");

        assertEquals("t1", tx.getId());
        assertEquals(500.0, tx.getAmount());
        assertEquals("DEBIT", tx.getType());
    }

    @Test
    void shouldCreatePrimaryContact() {
        PrimaryContact contact = new PrimaryContact();
        contact.setName("Raghav");
        contact.setEmail("r@abc.com");
        contact.setPhone("9999999999");

        assertEquals("Raghav", contact.getName());
        assertEquals("r@abc.com", contact.getEmail());
        assertEquals("9999999999", contact.getPhone());
    }

    @Test
    void shouldCreateCreditRequest() {
        CreditRequest cr = new CreditRequest();
        cr.setClientId("c1");
        cr.setRequestedAmount(500000);
        cr.setStatus(CreditRequestStatus.PENDING);

        assertEquals("c1", cr.getClientId());
        assertEquals(500000, cr.getRequestedAmount());
        assertEquals(CreditRequestStatus.PENDING, cr.getStatus());
    }

    @Test
    void shouldUseRoleEnum() {
        Role role = Role.RM;
        assertEquals("RM", role.name());
    }
}
