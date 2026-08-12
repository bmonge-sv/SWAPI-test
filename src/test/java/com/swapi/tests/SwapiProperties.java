package com.swapi.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads {@code swapi.properties} from the test classpath once and exposes
 * typed accessors grouped by resource. Values in the file are wrapped in
 * double-quotes (e.g. {@code people.name = "Luke Skywalker"}); the quotes are
 * stripped automatically so callers receive the bare string.
 */
public final class SwapiProperties {

    private static final String FILE = "swapi.properties";
    private static final Properties PROPS = load();

    private SwapiProperties() {}

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = SwapiProperties.class.getResourceAsStream("/" + FILE)) {
            if (in == null) {
                throw new IllegalStateException(FILE + " not found on the test classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load " + FILE, e);
        }
        return props;
    }

    /** Returns the value for {@code key}, stripping surrounding quotes. */
    public static String get(String key) {
        String raw = PROPS.getProperty(key);
        if (raw == null) {
            throw new IllegalArgumentException("Missing key in " + FILE + ": " + key);
        }
        return raw.trim().replaceAll("^\"|\"$", "");
    }

    /** Convenience overload that parses the value as an {@code int}. */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    // ── People ─────────────────────────────────────────────────────────────

    public static String peopleName()      { return get("people.name"); }
    public static String peopleHeight()    { return get("people.height"); }
    public static String peopleMass()      { return get("people.mass"); }
    public static String peopleHairColor() { return get("people.hair_color"); }
    public static String peopleSkinColor() { return get("people.skin_color"); }
    public static String peopleEyeColor()  { return get("people.eye_color"); }
    public static String peopleBirthYear() { return get("people.birth_year"); }
    public static String peopleGender()    { return get("people.gender"); }

    // ── Films ──────────────────────────────────────────────────────────────

    public static String filmsTitle()       { return get("films.title"); }
    public static int    filmsEpisodeId()   { return getInt("films.episode_id"); }
    public static String filmsDirector()    { return get("films.director"); }
    public static String filmsProducer()    { return get("films.producer"); }
    public static String filmsReleaseDate() { return get("films.release_date"); }

    // ── Planets ────────────────────────────────────────────────────────────

    public static String planetsName()           { return get("planets.name"); }
    public static String planetsRotationPeriod() { return get("planets.rotation_period"); }
    public static String planetsOrbitalPeriod()  { return get("planets.orbital_period"); }
    public static String planetsDiameter()       { return get("planets.diameter"); }
    public static String planetsClimate()        { return get("planets.climate"); }
    public static String planetsGravity()        { return get("planets.gravity"); }
    public static String planetsTerrain()        { return get("planets.terrain"); }
    public static String planetsSurfaceWater()   { return get("planets.surface_water"); }
    public static String planetsPopulation()     { return get("planets.population"); }

    // ── Species ────────────────────────────────────────────────────────────

    public static String speciesName()            { return get("species.name"); }
    public static String speciesClassification()  { return get("species.classification"); }
    public static String speciesDesignation()     { return get("species.designation"); }
    public static String speciesAverageHeight()   { return get("species.average_height"); }
    public static String speciesSkinColors()      { return get("species.skin_colors"); }
    public static String speciesHairColors()      { return get("species.hair_colors"); }
    public static String speciesEyeColors()       { return get("species.eye_colors"); }
    public static String speciesAverageLifespan() { return get("species.average_lifespan"); }
    public static String speciesLanguage()        { return get("species.language"); }

    // ── Starships ──────────────────────────────────────────────────────────

    public static String starshipsName()                 { return get("starships.name"); }
    public static String starshipsModel()                { return get("starships.model"); }
    public static String starshipsManufacturer()         { return get("starships.manufacturer"); }
    public static String starshipsCostInCredits()        { return get("starships.cost_in_credits"); }
    public static String starshipsLength()               { return get("starships.length"); }
    public static String starshipsMaxAtmospheringSpeed() { return get("starships.max_atmosphering_speed"); }
    public static String starshipsCrew()                 { return get("starships.crew"); }
    public static String starshipsPassengers()           { return get("starships.passengers"); }
    public static String starshipsCargoCapacity()        { return get("starships.cargo_capacity"); }

    // ── Vehicles ───────────────────────────────────────────────────────────

    public static String vehiclesName()                 { return get("vehicles.name"); }
    public static String vehiclesModel()                { return get("vehicles.model"); }
    public static String vehiclesManufacturer()         { return get("vehicles.manufacturer"); }
    public static String vehiclesCostInCredits()        { return get("vehicles.cost_in_credits"); }
    public static String vehiclesLength()               { return get("vehicles.length"); }
    public static String vehiclesMaxAtmospheringSpeed() { return get("vehicles.max_atmosphering_speed"); }
    public static String vehiclesCrew()                 { return get("vehicles.crew"); }
    public static String vehiclesPassengers()           { return get("vehicles.passengers"); }
}
