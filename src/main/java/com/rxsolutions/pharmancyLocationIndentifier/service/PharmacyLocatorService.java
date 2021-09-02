package com.rxsolutions.pharmancyLocationIndentifier.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorRequest;
import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is service class that will have all functional methods to calculate the
 * distance between origin and destinations
 */
@Service
public class PharmacyLocatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PharmacyLocatorService.class);

    // Autowire CsvLoaderService to get intance of the csv file loaded when needed.
    @Autowired
    CsvLoaderService csvLoaderService;

    /**
     * 1. Get the input pharmacy details list from CSV file. (Handled in CsvLoaderService).
     * 2. For each Pharmacy item in the list calculate the distance in Miles using Harvesine formula. 
     * 3. Once we get all pharmacies with total distatnce's calculated.
     * 4. Find the Pharmacy with minimum distance from list using util Comporator on distance.
     * 5. Return result back to the caller.
     * 
     * @param inputLatitude
     * @param inputLongitude
     * @return
     */

    public PharmacyLocatorResponse findNearestPharmacyLocation(double inputLatitude, double inputLongitude)

    {

        // Get all pharmacies from CSV file.
        List<PharmacyLocatorRequest> pharmacyListFromCSV = csvLoaderService.getPharmacyLocatorRequestList();

        List<PharmacyLocatorResponse> pharmacyLocatorResponseList = new ArrayList<>();

        for (PharmacyLocatorRequest pharmacy : pharmacyListFromCSV)

        {
                if (pharmacy != null)

                {
                        // For each PharmacyResponse calculate name, total address, distance,
                        PharmacyLocatorResponse pharmacyLocatorResponse = new PharmacyLocatorResponse();
                        pharmacyLocatorResponse.setName(
                                        pharmacy.getName() != null ? pharmacy.getName().trim() : pharmacy.getName());

                        // total address is a combincation of address, city, statet and zipcode.
                        pharmacyLocatorResponse.setAddress(pharmacyLocatorResponse.getCompleteAddress(
                                        pharmacy.getAddress() != null ? pharmacy.getAddress().trim() : "",
                                        pharmacy.getCity() != null ? pharmacy.getCity().trim() : "",
                                        pharmacy.getState() != null ? pharmacy.getState().trim() : "",
                                        String.valueOf(pharmacy.getZip())));

                        // distance in miles is calculated between pharmacy lat, long and user provided
                        // latitude and longitude.
                        pharmacyLocatorResponse
                                        .setTotalDistance(calculatePharmacyDistanceInMiles(pharmacy.getLatitude(),
                                                        pharmacy.getLongitude(), inputLatitude, inputLongitude));

                        LOGGER.info("Pharmacy name : " + pharmacyLocatorResponse.getName()
                                                        + " and calculated distance: " + pharmacyLocatorResponse.getTotalDistance());                                

                        pharmacyLocatorResponseList.add(pharmacyLocatorResponse);
                }
        }

        // once we get all calculated distances then we take the pharmacy with min
        // distance which will be our result PharmacyLocatorResponse object
        PharmacyLocatorResponse pharmacyLocatorResponse = Collections.min(pharmacyLocatorResponseList,
                Comparator.comparing(pharmacy -> pharmacy.getTotalDistance()));

        LOGGER.info("Nearest pharmacy for given input lat and long : " + pharmacyLocatorResponse.getName()
                + " and distance: " + pharmacyLocatorResponse.getTotalDistance());

        return pharmacyLocatorResponse;

    }

    /*
     * Calculate the distance based on the Pharmacy latitude and longitude and user
     * provided input latitude and longitude. We are using Haversine Formula
     * Haversine Formula:
     * 
     * (d/r) = sin^2(lat2-lat1/2) + cos(lat1) * cos (lat2) * sin^2(lon2-long1/2) -->
     * d = 2 * r * asin (sqrt(a))
     * 
     * @param pharmacyLatitude
     * 
     * @param pharmacyLongitude
     * 
     * @param inputLatitude
     * 
     * @param inputLongitude
     * 
     * @return
     */
    public double calculatePharmacyDistanceInMiles(double pharmacyLatitude, double pharmacyLongitude,
            double inputLatitude, double inputLongitude) {

        if (pharmacyLatitude == inputLatitude && pharmacyLongitude == inputLongitude) {
            return 0;
        }

        else {

            // caluculate destination - origin latitude difference
            double distanceInLat = Math.toRadians(pharmacyLatitude - inputLatitude);
            // caluculate destination - origin longitude difference
            double distanceInLong = Math.toRadians(pharmacyLongitude - inputLongitude);

            // lets compute (d/r) = sin2(lat2-lat1/2) + cos(lat1) * cos (lat2) *
            // sin2(lon2-long1/2) --> a
            double distanceByR = Math.sin(distanceInLat / 2) * Math.sin(distanceInLat / 2)
                    + Math.cos(Math.toRadians(inputLatitude)) * Math.cos(Math.toRadians(pharmacyLatitude))
                            * Math.sin(distanceInLong / 2) * Math.sin(distanceInLong / 2);

            // radius of earth is 6371 KMs
            double radiusOfEarth = 6371;

            // r = radius of earth.
            // now calculate: d = 2 Math.asin(sqrt(a)) * r
            double c = 2 * Math.asin(Math.sqrt(distanceByR));

            double distanceInKm = radiusOfEarth * c;

            // Miles = KM * 0.62137
            double distnaceInMiles = distanceInKm * 0.62137;
            return distnaceInMiles;

        }

    }

}