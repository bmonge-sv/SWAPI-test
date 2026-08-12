package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Person extends BaseModel {

    private String name;
    private String height;
    private String mass;

    @JsonProperty("hair_color")
    private String hairColor;

    @JsonProperty("skin_color")
    private String skinColor;

    @JsonProperty("eye_color")
    private String eyeColor;

    @JsonProperty("birth_year")
    private String birthYear;

    private String gender;
    private String homeworld;

    private List<String> films;
    private List<String> species;
    private List<String> vehicles;
    private List<String> starships;

    public String       getName()      { return name; }
    public String       getHeight()    { return height; }
    public String       getMass()      { return mass; }
    public String       getHairColor() { return hairColor; }
    public String       getSkinColor() { return skinColor; }
    public String       getEyeColor()  { return eyeColor; }
    public String       getBirthYear() { return birthYear; }
    public String       getGender()    { return gender; }
    public String       getHomeworld() { return homeworld; }
    public List<String> getFilms()     { return films; }
    public List<String> getSpecies()   { return species; }
    public List<String> getVehicles()  { return vehicles; }
    public List<String> getStarships() { return starships; }

    public void setName(String name)                   { this.name = name; }
    public void setHeight(String height)               { this.height = height; }
    public void setMass(String mass)                   { this.mass = mass; }
    public void setHairColor(String hairColor)         { this.hairColor = hairColor; }
    public void setSkinColor(String skinColor)         { this.skinColor = skinColor; }
    public void setEyeColor(String eyeColor)           { this.eyeColor = eyeColor; }
    public void setBirthYear(String birthYear)         { this.birthYear = birthYear; }
    public void setGender(String gender)               { this.gender = gender; }
    public void setHomeworld(String homeworld)         { this.homeworld = homeworld; }
    public void setFilms(List<String> films)           { this.films = films; }
    public void setSpecies(List<String> species)       { this.species = species; }
    public void setVehicles(List<String> vehicles)     { this.vehicles = vehicles; }
    public void setStarships(List<String> starships)   { this.starships = starships; }
}
