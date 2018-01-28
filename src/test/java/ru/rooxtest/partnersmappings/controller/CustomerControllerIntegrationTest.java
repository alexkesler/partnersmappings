package ru.rooxtest.partnersmappings.controller;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.rooxtest.partnersmappings.security.AuthorizationFilter;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для котроллера абонентов
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/database-context.xml", "/WEB-INF/security-context.xml","/WEB-INF/partners-servlet.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private AuthorizationFilter authorizationFilter;
    private MockMvc mockMvc;

    @Before
    public  void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(authorizationFilter).build();
    }

    @Test
    public void test00CustomerControllerIsInitialised() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("customerController"));
    }

    @Test
    public void test10GetCustomersWithoutAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test11GetCustomersWithWrongAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers")
                .header("Authorization","Bearer"+"12345"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test12GetCustomersWithWrongAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-111222333444"))
                .andDo(print()).andExpect(status().isForbidden());

    }


    @Test
    public void test20GetCustomersWithAuthIsOkAndIsArrayAndArrayLengthIs3() throws Exception {
        this.mockMvc.perform(get("/customers")
                            .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    public void test30GetCustomerWithoutAuthByWrongIdIsNotFound() throws Exception {
        this.mockMvc.perform(get("/customers/12345"))
                .andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    public void test31GetCustomerWithAuthByWrongIdIsNotFound() throws Exception {
        this.mockMvc.perform(get("/customers/12345")
                            .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    public void test40GetCustomerByIdIsOkAndIsCorrectId() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0")
                            .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5a60a081-b940-4dae-b12f-b11d6db2dfb0"));

    }

    @Test
    public void test41GetCustomerByMeIsOkAndIsCorrectId() throws Exception {
        this.mockMvc.perform(get("/customers/@me")
                            .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5a60a081-b940-4dae-b12f-b11d6db2dfb0"));

    }

}
