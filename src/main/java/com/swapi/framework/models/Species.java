package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Species extends BaseModel {

    private String name;
    private String classification;
    private String designation;

    @JsonProperty("average_height")
    private String averageHeight;

    @JsonProperty("skin_colors")
    private String skinColors;

    @JsonProperty("hair_colors")
    private String hairColors;

    @JsonProperty("eye_colors")
    private String eyeColors;

    @JsonProperty("average_lifespan")
    private String averageLifespan;

    private String homeworld;
    private String language;

    private List<String> people;
    private List<String> films;

    public String       getName()            { return name; }
    public String       getClassification()  { return classification; }
    public String       getDesignation()     { return designation; }
    public String       getAverageHeight()   { return averageHeight; }
    public String       getSkinColors()      { return skinColors; }
    public String       getHairColors()      { return hairColors; }
    public String       getEyeColors()       { return eyeColors; }
    public String       getAverageLifespan() { return averageLifespan; }
    public String       getHomeworld()       { return homeworld; }
    public String       getLanguage()        { return language; }
    public List<String> getPeople()          { return people; }
    public List<String> getFilms()           { return films; }

    public void setName(String name)                       { this.name = name; }
    public void setClassification(String classification)   { this.classification = classification; }
    public void setDesignation(String designation)         { this.designation = designation; }
    public void setAverageHeight(String averageHeight)     { this.averageHeight = averageHeight; }
    public void setSkinColors(String skinColors)           { this.skinColors = skinColors; }
    public void setHairColors(String hairColors)           { this.hairColors = hairColors; }
    public void setEyeColors(String eyeColors)             { this.eyeColors = eyeColors; }
    public void setAverageLifespan(String averageLifespan) { this.averageLifespan = averageLifespan; }
    public void setHomeworld(String homeworld)             { this.homeworld = homeworld; }
    public void setLanguage(String language)               { this.language = language; }
    public void setPeople(List<String> people)             { this.people = people; }
    public void setFilms(List<String> films)               { this.films = films; }
}
