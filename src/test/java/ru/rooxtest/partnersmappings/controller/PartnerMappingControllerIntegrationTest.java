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
import ru.rooxtest.partnersmappings.security.AuthorizationFilter;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для контроллера привязок
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/database-context.xml", "/WEB-INF/security-context.xml","/WEB-INF/partners-servlet.xml"})
@WebAppConfiguration(value = "classpath:")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartnerMappingControllerIntegrationTest {

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
    public void test00PartnerMappingControllerIsInitialised() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("partnerMappingController"));
    }

    @Test
    public void test10GetPartnerMappingsWithoutAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings"))
                .andDo(print()).andExpect(status().isForbidden());

    }


    @Test
    public void test11GetPartnerMappingsWithWrongAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"12345"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test12GetPartnerMappingsWithWrongAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-111222333444"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test13GetPartnerMappingsWithNotFitAuthIsForbidden() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"86b57ac7-1cdb-4cf6-ba43-31b6c417c35e"))
                .andDo(print()).andExpect(status().isForbidden());

    }

    @Test
    public void test20GetPartnerMappingsWithFitAuthIsOkAndIsArrayAndSizeIs0() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void test30PostPartnerMappingWithAuthIsCreated() throws Exception {

        this.mockMvc.perform(post("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                    .content("{" +
                                "\"id\":\"cb43d94d-0de9-4a4d-92e9-f5bbf243560b\"," +
                                "\"customerId\":\"5a60a081-b940-4dae-b12f-b11d6db2dfb0\"," +
                                "\"partnerId\":\"app123456\"," +
                                "\"accountId\":\"/id1223344\"," +
                                "\"fio\":\"ИвановИИ\"" +
                            "}")
                    .contentType("application/json;charset=UTF-8")
                    .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    public void test31GetPartnerMappingWithFitAuthIsOkAndIsArrayAndSizeIs1AndFitFio() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0" +
                "/partnermappings/cb43d94d-0de9-4a4d-92e9-f5bbf243560b")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("ИвановИИ"));

    }

    @Test
    public void test32PutPartnerMappingWithChangedFioWithAuthIsOk() throws Exception {

        this.mockMvc.perform(put("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0" +
                                            "/partnermappings/cb43d94d-0de9-4a4d-92e9-f5bbf243560b")
                .content("{" +
                        "\"id\":\"cb43d94d-0de9-4a4d-92e9-f5bbf243560b\"," +
                        "\"customerId\":\"5a60a081-b940-4dae-b12f-b11d6db2dfb0\"," +
                        "\"partnerId\":\"app123456\"," +
                        "\"accountId\":\"/id1223344\"," +
                        "\"fio\":\"Иванов И.И.\"" +
                        "}")
                .contentType("application/json;charset=UTF-8")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void test33GetPartnerMappingWithFitAuthIsOkAndIsArrayAndSizeIs1AndChangedFio() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0" +
                                            "/partnermappings/cb43d94d-0de9-4a4d-92e9-f5bbf243560b")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Иванов И.И."));

    }

    @Test
    public void test34DeletePartnerMappingsWithFitAuthIsOkAndReturnDeleted() throws Exception {
        this.mockMvc.perform(delete("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0" +
                                                "/partnermappings/cb43d94d-0de9-4a4d-92e9-f5bbf243560b")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cb43d94d-0de9-4a4d-92e9-f5bbf243560b"));

    }

    @Test
    public void test35GetPartnerMappingsWithFitAuthIsOkAndIsArrayAndSizeIs0() throws Exception {
        this.mockMvc.perform(get("/customers/5a60a081-b940-4dae-b12f-b11d6db2dfb0/partnermappings")
                .header("Authorization","Bearer"+"5a60a081-b940-4dae-b12f-b11d6db2dfb0"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

    }

}
