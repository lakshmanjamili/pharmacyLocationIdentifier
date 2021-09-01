package com.rxsolutions.pharmancyLocationIndentifier.controller;

import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorResponse;
import com.rxsolutions.pharmancyLocationIndentifier.service.PharmacyLocatorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller class to handle API requests.
 * Author : Lakshman Jamili
 */
@Controller
public class PharmacyLocatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PharmacyLocatorController.class);

    // Autowiring the locator service to make calls to service methods.
    @Autowired
    PharmacyLocatorService pharmacyLocatorService;

    /**
     * 
     * @param latitude
     * @param latitude
     * @return
     */
    @GetMapping("/nearestpharmacylocation")
    public ResponseEntity<PharmacyLocatorResponse> findNearestPharmacyLocation(
            @RequestParam(value = "latitude", required = true) Double inputLatitude,
            @RequestParam(value = "longitude", required = true) Double inputLongitude) {

        LOGGER.info("Request recieved to pharmacylocation api latitude : " + inputLatitude + " and longitude :"
                + inputLongitude);

        PharmacyLocatorResponse pharmacyLocatorResponse = null;
        try {

            // get the pharmacy locator response after calculating the shortest distance.
            pharmacyLocatorResponse = pharmacyLocatorService.findNearestPharmacyLocation(inputLatitude, inputLongitude);

        }

        catch (Exception e)

        {

            // if there is an exception , we will respond with internal server error.
            LOGGER.error("Error while calculating latitude : " + inputLatitude + "longitude :" + inputLongitude);
            return new ResponseEntity<>(pharmacyLocatorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(pharmacyLocatorResponse, HttpStatus.OK);

    }

}