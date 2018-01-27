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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для контроллера привязок
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/databaseContext.xml","/WEB-INF/securityContext.xml","/WEB-INF/partners-servlet.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartnerMappingControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public  void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test1PartnerMappingControllerIsInitialised() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("partnerMappingController"));
    }

    @Test
    public void test2GetPartnerMappingsWithoutAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings"))
                .andDo(print()).andExpect(status().isForbidden());

    }


    @Test
    public void test3GetPartnerMappingsWithWrongAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-111222333444"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test3GetPartnerMappingsWithAuthIsOkAndIsArray() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void test5PostPartnerMappingWithoutAuthIsForbidden() throws Exception {
        this.mockMvc.perform(post("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .content(""))
                .andDo(print()).andExpect(status().isForbidden());

    }

}
