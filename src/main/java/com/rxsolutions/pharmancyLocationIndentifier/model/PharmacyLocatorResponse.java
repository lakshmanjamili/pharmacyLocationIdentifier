package com.rxsolutions.pharmancyLocationIndentifier.model;

/**
 * Pharmacy Locator Response consits of requested output fields name address
 * total distance calculated on input latitude/longitude and location of the
 * pharmacy.
 * 
 */
public class PharmacyLocatorResponse {

    private String name;
    private String address;
    private double totalDistance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * Get complete address by appendinng address, city, state and zip.
     * 
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     */
    public String getCompleteAddress(String address, String city, String state, String zip) {

        return address + " " + city + " " + state + " " + zip;
    }

}