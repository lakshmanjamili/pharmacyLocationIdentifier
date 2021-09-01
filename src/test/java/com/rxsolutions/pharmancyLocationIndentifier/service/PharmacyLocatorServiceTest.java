package com.rxsolutions.pharmancyLocationIndentifier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorRequest;
import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PharmacyLocatorServiceTest {

    @InjectMocks
    private PharmacyLocatorService pharmacyLocatorService;

    @Mock
    private CsvLoaderService csvLoaderService;

    @Test

    /**
     * Test CalculatePharmacyDistanceInMiles method that will compute the distance
     * in miles.
     */
    public void testCalculatePharmacyDistanceInMiles()

    {

        double distanceBetweenOriginDestination = pharmacyLocatorService.calculatePharmacyDistanceInMiles(39.001423,
                -95.68695, 38.852390, -94.722740);

        assertEquals(distanceBetweenOriginDestination, 52.83993102735984);

    }

    @Test
    public void testCalculatePharmacyDistanceAllEqualDistance()

    {

        double distanceBetweenOriginDestination = pharmacyLocatorService.calculatePharmacyDistanceInMiles(39.001423,
                -95.68695, 39.001423, -95.68695);

        assertEquals(distanceBetweenOriginDestination, 0);

    }

    /**
     * calculating the distance among all origin and destination and find the
     * minimum distance result.
     */
    @Test
    public void testFindNearestPharmacyLocation()

    {

        List<PharmacyLocatorRequest> pharmacyListFromCSV = new ArrayList<>();
        PharmacyLocatorRequest pharmacyLocatorRequest = new PharmacyLocatorRequest();
        pharmacyLocatorRequest.setAddress("3696 SW TOPEKA BLVD");
        pharmacyLocatorRequest.setCity("TOPEKA");
        pharmacyLocatorRequest.setState("KS");
        pharmacyLocatorRequest.setZip(66221);
        pharmacyLocatorRequest.setLatitude(39.001423);
        pharmacyLocatorRequest.setLongitude(-95.68695);
        pharmacyLocatorRequest.setName("Walgreens");

        pharmacyListFromCSV.add(pharmacyLocatorRequest);

        when(csvLoaderService.getPharmacyLocatorRequestList()).thenReturn(pharmacyListFromCSV);

        PharmacyLocatorResponse pharmacyLocatorResponse = pharmacyLocatorService.findNearestPharmacyLocation(38.852390,
                -94.722740);
        assertEquals(pharmacyLocatorResponse.getTotalDistance(), 52.83993102735984);
        assertEquals(pharmacyLocatorResponse.getName(), "Walgreens");
        assertEquals(pharmacyLocatorResponse.getAddress(), "3696 SW TOPEKA BLVD TOPEKA KS 66221");

    }

    @Test
    public void testFindNearestPharmacyLocationEqualDistanceLatLong()

    {

        List<PharmacyLocatorRequest> pharmacyListFromCSV = new ArrayList<>();
        PharmacyLocatorRequest pharmacyLocatorRequest = new PharmacyLocatorRequest();
        pharmacyLocatorRequest.setAddress("3696 SW TOPEKA BLVD");
        pharmacyLocatorRequest.setCity("TOPEKA");
        pharmacyLocatorRequest.setState("KS");
        pharmacyLocatorRequest.setZip(66221);
        pharmacyLocatorRequest.setLatitude(39.001423);
        pharmacyLocatorRequest.setLongitude(-95.68695);
        pharmacyLocatorRequest.setName("Walgreens");

        pharmacyListFromCSV.add(pharmacyLocatorRequest);

        pharmacyLocatorRequest = new PharmacyLocatorRequest();
        pharmacyLocatorRequest.setAddress("11800 W 142nd st");
        pharmacyLocatorRequest.setCity("Overland Park");
        pharmacyLocatorRequest.setState("KS");
        pharmacyLocatorRequest.setZip(66221);
        pharmacyLocatorRequest.setLatitude(38.852390);
        pharmacyLocatorRequest.setLongitude(-94.722740);
        pharmacyLocatorRequest.setName("Walgreens");
        pharmacyListFromCSV.add(pharmacyLocatorRequest);

        when(csvLoaderService.getPharmacyLocatorRequestList()).thenReturn(pharmacyListFromCSV);

        PharmacyLocatorResponse pharmacyLocatorResponse = pharmacyLocatorService.findNearestPharmacyLocation(38.852390,
                -94.722740);
        assertEquals(pharmacyLocatorResponse.getTotalDistance(), 0);
        assertEquals(pharmacyLocatorResponse.getName(), "Walgreens");
        assertEquals(pharmacyLocatorResponse.getAddress(), "11800 W 142nd st Overland Park KS 66221");

    }

}