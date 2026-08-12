package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Planet extends BaseModel {

    private String name;

    @JsonProperty("rotation_period")
    private String rotationPeriod;

    @JsonProperty("orbital_period")
    private String orbitalPeriod;

    private String diameter;
    private String climate;
    private String gravity;
    private String terrain;

    @JsonProperty("surface_water")
    private String surfaceWater;

    private String population;

    private List<String> residents;
    private List<String> films;

    public String       getName()           { return name; }
    public String       getRotationPeriod() { return rotationPeriod; }
    public String       getOrbitalPeriod()  { return orbitalPeriod; }
    public String       getDiameter()       { return diameter; }
    public String       getClimate()        { return climate; }
    public String       getGravity()        { return gravity; }
    public String       getTerrain()        { return terrain; }
    public String       getSurfaceWater()   { return surfaceWater; }
    public String       getPopulation()     { return population; }
    public List<String> getResidents()      { return residents; }
    public List<String> getFilms()          { return films; }

    public void setName(String name)                    { this.name = name; }
    public void setRotationPeriod(String rotationPeriod){ this.rotationPeriod = rotationPeriod; }
    public void setOrbitalPeriod(String orbitalPeriod)  { this.orbitalPeriod = orbitalPeriod; }
    public void setDiameter(String diameter)            { this.diameter = diameter; }
    public void setClimate(String climate)              { this.climate = climate; }
    public void setGravity(String gravity)              { this.gravity = gravity; }
    public void setTerrain(String terrain)              { this.terrain = terrain; }
    public void setSurfaceWater(String surfaceWater)    { this.surfaceWater = surfaceWater; }
    public void setPopulation(String population)        { this.population = population; }
    public void setResidents(List<String> residents)    { this.residents = residents; }
    public void setFilms(List<String> films)            { this.films = films; }
}
