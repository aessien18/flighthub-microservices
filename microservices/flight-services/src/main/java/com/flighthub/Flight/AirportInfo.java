package com.flighthub.Flight;

public class AirportInfo {
    private String name;
    private String iata;
    private String icao;
    private double latitude;
    private double longitude;
    private String city;
    private String country;

    public AirportInfo(String name, String iata, String icao, double latitude, double longitude, String city, String country) {
        this.name = name;
        this.iata = iata;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public String getName() { return name; }
    public String getIata() { return iata; }
    public String getIcao() { return icao; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
}
