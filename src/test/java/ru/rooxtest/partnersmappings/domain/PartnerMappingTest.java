package ru.rooxtest.partnersmappings.domain;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartnerMappingTest {

    private PartnerMapping partnerMapping;
    private PartnerMapping partnerMapping1;
    private PartnerMapping partnerMapping2;

    @Before
    public void init() {
        partnerMapping = new PartnerMapping();
        partnerMapping1 = new PartnerMapping();
        partnerMapping2 = new PartnerMapping();
    }


    @Test
    public void checkIdNotNull() {
        assertNotNull(partnerMapping.getId());
    }

    @Test
    public void testUnique() {
        assertNotEquals(partnerMapping1,partnerMapping2);
    }
}
