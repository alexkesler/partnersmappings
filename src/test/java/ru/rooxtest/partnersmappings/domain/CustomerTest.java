package ru.rooxtest.partnersmappings.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CustomerTest {

    private Customer customer;
    private Customer customer1;
    private Customer customer2;


    @Before
    public void initTest() {
        customer = new Customer();
        customer1 = new Customer();
        customer2 = new Customer();
    }


    @After
    public void afterTest() {
        customer = null;
        customer1 = null;
        customer2 = null;
    }

    @Test
    public void testIdNotNull() {
        assertNotNull(customer.id);
    }

    @Test
    public void testUnique() {
        assertNotEquals(customer1,customer2);
     }



}
