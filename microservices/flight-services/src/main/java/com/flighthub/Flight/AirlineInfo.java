package com.flighthub.Flight;

public class AirlineInfo {
    private String name;
    private String iata;
    private String icao;
    private String logo;

    public AirlineInfo() {
    }

    public AirlineInfo(String name, String iata, String icao, String logo) {
        this.name = name;
        this.iata = iata;
        this.icao = icao;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

