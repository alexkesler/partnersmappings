package ru.rooxtest.partnersmappings.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void test1IdNotNull() {
        assertNotNull(customer.id);
    }

    @Test
    public void test2Unique() {
        assertNotEquals(customer1,customer2);
     }



}
