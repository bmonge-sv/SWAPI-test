package com.swapi.framework.constants;

/**
 * Resource paths, relative to the configured base path ({@code /api}).
 * Trailing slashes are intentional: SWAPI runs on Django REST Framework, which
 * redirects slash-less URLs and can turn a POST into a GET during that redirect.
 */
public final class Endpoints {

    public static final String PLANETS = "/planets/";
    public static final String STARSHIPS = "/starships/";
    public static final String VEHICLES = "/vehicles/";
    public static final String PEOPLE = "/people/";
    public static final String FILMS = "/films/";
    public static final String SPECIES = "/species/";

    /** Appended to a collection path to address a single item, e.g. {/planets/{id}/}. */
    public static final String BY_ID = "{id}/";

    private Endpoints() {
        // Constants holder; no instances.
    }
}
