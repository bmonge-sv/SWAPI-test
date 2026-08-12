package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Film extends BaseModel {

    private String title;

    @JsonProperty("episode_id")
    private int episodeId;

    @JsonProperty("opening_crawl")
    private String openingCrawl;

    private String director;
    private String producer;

    @JsonProperty("release_date")
    private String releaseDate;

    private List<String> characters;
    private List<String> planets;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> species;

    public String       getTitle()        { return title; }
    public int          getEpisodeId()    { return episodeId; }
    public String       getOpeningCrawl() { return openingCrawl; }
    public String       getDirector()     { return director; }
    public String       getProducer()     { return producer; }
    public String       getReleaseDate()  { return releaseDate; }
    public List<String> getCharacters()   { return characters; }
    public List<String> getPlanets()      { return planets; }
    public List<String> getStarships()    { return starships; }
    public List<String> getVehicles()     { return vehicles; }
    public List<String> getSpecies()      { return species; }

    public void setTitle(String title)                    { this.title = title; }
    public void setEpisodeId(int episodeId)               { this.episodeId = episodeId; }
    public void setOpeningCrawl(String openingCrawl)      { this.openingCrawl = openingCrawl; }
    public void setDirector(String director)              { this.director = director; }
    public void setProducer(String producer)              { this.producer = producer; }
    public void setReleaseDate(String releaseDate)        { this.releaseDate = releaseDate; }
    public void setCharacters(List<String> characters)    { this.characters = characters; }
    public void setPlanets(List<String> planets)          { this.planets = planets; }
    public void setStarships(List<String> starships)      { this.starships = starships; }
    public void setVehicles(List<String> vehicles)        { this.vehicles = vehicles; }
    public void setSpecies(List<String> species)          { this.species = species; }
}
