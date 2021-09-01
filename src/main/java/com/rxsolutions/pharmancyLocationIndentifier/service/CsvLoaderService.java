package com.rxsolutions.pharmancyLocationIndentifier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rxsolutions.pharmancyLocationIndentifier.model.PharmacyLocatorRequest;

import org.slf4j.Logger;

/**
 * Csv loader service is used to load pharmacies.csv as that is the input with
 * all pharmacy details. Read the resource from the class path using open csv
 * then create a list of PharmacyLocatorRequest as each row in csv is a
 * PharmacyLocatorRequest item.
 */
@Service
public class CsvLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvLoaderService.class);

    // store each csv row item as PharmacyLocatorRequest item in the arraylist.

    List<PharmacyLocatorRequest> pharmacyLocatorRequestList = new ArrayList<>();

    // Resourse loader is used to read files from classpath.
    // The pharmacies.csv is kept in resources folder in class path
    @Autowired
    ResourceLoader resourceLoader;

    /**
     * Loadcsvfile will be loaded during the time of bootstrap and populate
     * setPharmacyLocatorRequestList() with the values, that we dont need to read
     * CSV file for every user call.
     */
    @PostConstruct
    public void loadCsvFile() {

        // Finds the resources with pharmacies.csv value.
        final Resource resource = resourceLoader.getResource("classpath:pharmacies.csv");
        try {

            LOGGER.info("Reading pharmacies csv file");
            // reading the file using Open csv dependency.
            setPharmacyLocatorRequestList(readCsvFile(resource.getInputStream()));

        }

        catch (final Exception e) {

            LOGGER.error("Unable to find the class path or stream is null", e);

        }

    }

    /**
     * 
     * Read CSV file using Open csv library and store it in a ArrayList of
     * PharmacyLocatorRequest type.
     * 
     * @param inputStream
     * @return
     */

    public List<PharmacyLocatorRequest> readCsvFile(InputStream inputStream)

    {

        List<PharmacyLocatorRequest> pharmacyLocatorRequestList = new ArrayList<>();

        // Used to read characters of stream provided.
        Reader reader = new InputStreamReader(inputStream);

        // Parameters of openCSV column mapping strategy
        ColumnPositionMappingStrategy columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
        columnPositionMappingStrategy.setType(PharmacyLocatorRequest.class);

        // fields of the input csv file.
        String[] csvFields = { "name", "address", "city", "state", "zip", "latitude", "longitude" };
        columnPositionMappingStrategy.setColumnMapping(csvFields);

        // convert the CSV to bean (model) with mapping strategy , removing empty white
        // spaces, skip header line etc.
        CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(PharmacyLocatorRequest.class)
                .withMappingStrategy(columnPositionMappingStrategy).withSkipLines(1).withIgnoreLeadingWhiteSpace(true)
                .build();

        // Iterate over csvToBean object and compute the csv rows to a List of items.
        Iterator<PharmacyLocatorRequest> iterator = csvToBean.iterator();
        iterator.forEachRemaining(pharmacyLocatorRequest -> pharmacyLocatorRequestList.add(pharmacyLocatorRequest));

        return pharmacyLocatorRequestList;

    }

    public List<PharmacyLocatorRequest> getPharmacyLocatorRequestList() {
        return pharmacyLocatorRequestList;
    }

    public void setPharmacyLocatorRequestList(final List<PharmacyLocatorRequest> pharmacyLocatorRequestList) {
        this.pharmacyLocatorRequestList = pharmacyLocatorRequestList;
    }

}