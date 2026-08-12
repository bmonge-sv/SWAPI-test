package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Starship extends BaseModel {

    private String name;
    private String model;
    private String manufacturer;

    @JsonProperty("cost_in_credits")
    private String costInCredits;

    private String length;

    @JsonProperty("max_atmosphering_speed")
    private String maxAtmospheringSpeed;

    private String crew;
    private String passengers;

    @JsonProperty("cargo_capacity")
    private String cargoCapacity;

    private String consumables;

    @JsonProperty("hyperdrive_rating")
    private String hyperdriveRating;

    @JsonProperty("MGLT")
    private String mglt;

    @JsonProperty("starship_class")
    private String starshipClass;

    private List<String> pilots;
    private List<String> films;

    public String       getName()                 { return name; }
    public String       getModel()                { return model; }
    public String       getManufacturer()         { return manufacturer; }
    public String       getCostInCredits()        { return costInCredits; }
    public String       getLength()               { return length; }
    public String       getMaxAtmospheringSpeed() { return maxAtmospheringSpeed; }
    public String       getCrew()                 { return crew; }
    public String       getPassengers()           { return passengers; }
    public String       getCargoCapacity()        { return cargoCapacity; }
    public String       getConsumables()          { return consumables; }
    public String       getHyperdriveRating()     { return hyperdriveRating; }
    public String       getMglt()                 { return mglt; }
    public String       getStarshipClass()        { return starshipClass; }
    public List<String> getPilots()               { return pilots; }
    public List<String> getFilms()                { return films; }

    public void setName(String name)                                 { this.name = name; }
    public void setModel(String model)                               { this.model = model; }
    public void setManufacturer(String manufacturer)                 { this.manufacturer = manufacturer; }
    public void setCostInCredits(String costInCredits)               { this.costInCredits = costInCredits; }
    public void setLength(String length)                             { this.length = length; }
    public void setMaxAtmospheringSpeed(String maxAtmospheringSpeed) { this.maxAtmospheringSpeed = maxAtmospheringSpeed; }
    public void setCrew(String crew)                                 { this.crew = crew; }
    public void setPassengers(String passengers)                     { this.passengers = passengers; }
    public void setCargoCapacity(String cargoCapacity)               { this.cargoCapacity = cargoCapacity; }
    public void setConsumables(String consumables)                   { this.consumables = consumables; }
    public void setHyperdriveRating(String hyperdriveRating)         { this.hyperdriveRating = hyperdriveRating; }
    public void setMglt(String mglt)                                 { this.mglt = mglt; }
    public void setStarshipClass(String starshipClass)               { this.starshipClass = starshipClass; }
    public void setPilots(List<String> pilots)                       { this.pilots = pilots; }
    public void setFilms(List<String> films)                         { this.films = films; }
}
