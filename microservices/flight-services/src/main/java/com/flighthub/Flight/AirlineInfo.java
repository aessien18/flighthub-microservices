package com.flighthub.Flight;

public class AirlineInfo {
    private String name;
    private String iata;
    private String icao;
    private String logo;

    public AirlineInfo(String name, String iata, String icao, String logo) {
        this.name = name;
        this.iata = iata;
        this.icao = icao;
        this.logo = logo;
    }

    public String getName() { return name; }
    public String getIata() { return iata; }
    public String getIcao() { return icao; }
    public String getLogo() { return logo; }
}
