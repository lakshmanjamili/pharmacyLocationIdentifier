package com.rxsolutions.pharmancyLocationIndentifier.controller;

import static org.mockito.Mockito.when;

import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorResponse;
import com.rxsolutions.pharmancyLocationIndentifier.service.PharmacyLocatorService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PharmacyLocatorController.class)
public class PharmacyLocatorControllerTest {

    @MockBean
    private PharmacyLocatorService pharmacyLocatorService;

    @Autowired
    MockMvc mockMvc;

    /**
     * Test nearest Pharmacy location with valid values of lat and long
     * 
     * @throws Exception
     */
    @Test
    public void testFindNearestPharmacyLocation() throws Exception

    {

        PharmacyLocatorResponse pharmacyLocatorResponse = new PharmacyLocatorResponse();

        pharmacyLocatorResponse.setName("Walgreens");
        pharmacyLocatorResponse.setAddress(
                pharmacyLocatorResponse.getCompleteAddress("11800 W 148th", "Overland Park", "KS", "66221"));
        pharmacyLocatorResponse.setTotalDistance(123.4232);

        when(pharmacyLocatorService.findNearestPharmacyLocation(1323, 13231)).thenReturn(pharmacyLocatorResponse);

        this.mockMvc.perform(get("/nearestpharmacylocation").param("latitude", "1323").param("longitude", "13231"))
                .andDo(print()).andExpect(status().isOk());

    }

    /**
     * Test nearest pharamacy location when params are missing it should it
     * exception handler controller
     * 
     * @throws Exception
     */
    @Test
    public void testFindNearestPharmacyLocationWithParamMissing() throws Exception

    {

        this.mockMvc.perform(get("/nearestpharmacylocation")).andDo(print()).andExpect(status().isBadRequest());

    }

}