package ru.rooxtest.partnersmappings.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для котроллера абонентов
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/databaseContext.xml","/WEB-INF/securityContext.xml","/WEB-INF/partners-servlet.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public  void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test1CustomerControllerIsInitialised() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("customerController"));
    }

    @Test
    public void test2GetCustomersIsOkAndIsArrayAndArrayLengthIs3() throws Exception {
        this.mockMvc.perform(get("/customers"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    public void test3GetCustomerIsOkAndIsCorrectId() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5a60a081-b940-4dae-b12f-b11d6db2dfb0"));

    }



}
