package com.swapi.tests;

import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.swapi.framework.core.BaseEndpoint;
import com.swapi.framework.core.HttpStatus;
import com.swapi.framework.endpoints.PlanetEndpoint;
import com.swapi.framework.models.Planet;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("Planets")
public class PlanetTest extends AbstractResourceTest {

    private final PlanetEndpoint planets = new PlanetEndpoint();
    private Planet planet;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return planets; }
    @Override protected int          validId()      { return 1; }
    @Override protected String       resourceName() { return "planets"; }

    /** Fetches planet 1 once; all verify* tests share this cached model. */
    @BeforeClass(groups = "planet")
    public void fetchPlanet() {
        response = planets.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "planets/" + validId() + " must return 200 before field assertions can run");
        planet = response.as(Planet.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "planet", description = "GET by id deserializes into the Planet model")
    public void getById_deserializesToPlanet() {
        assertNotNull(planet, "Planet model must not be null");
        assertFalse(planet.getFilms().isEmpty(), "Tatooine should appear in at least one film");
    }

    @Story("Validation of Planet name")
    @Test(groups = "planet", description = "name matches swapi.properties")
    public void verifyName() {
        assertEquals(planet.getName(), SwapiProperties.planetsName());
    }

    @Story("Validation of planet rotation period")
    @Test(groups = "planet", description = "rotation_period matches swapi.properties")
    public void verifyRotationPeriod() {
        assertEquals(planet.getRotationPeriod(), SwapiProperties.planetsRotationPeriod());
    }

    @Story("Validation of planet orbital period")
    @Test(groups = "planet", description = "orbital_period matches swapi.properties")
    public void verifyOrbitalPeriod() {
        assertEquals(planet.getOrbitalPeriod(), SwapiProperties.planetsOrbitalPeriod());
    }

    @Story("Validation of planet diameter")
    @Test(groups = "planet", description = "diameter matches swapi.properties")
    public void verifyDiameter() {
        assertEquals(planet.getDiameter(), SwapiProperties.planetsDiameter());
    }

    @Story("Validation of planet climate")
    @Test(groups = "planet", description = "climate matches swapi.properties")
    public void verifyClimate() {
        assertEquals(planet.getClimate(), SwapiProperties.planetsClimate());
    }

    @Story("Validation of planet gravity")
    @Test(groups = "planet", description = "gravity matches swapi.properties")
    public void verifyGravity() {
        assertEquals(planet.getGravity(), SwapiProperties.planetsGravity());
    }

    @Story("Validation of planet terrain")
    @Test(groups = "planet", description = "terrain matches swapi.properties")
    public void verifyTerrain() {
        assertEquals(planet.getTerrain(), SwapiProperties.planetsTerrain());
    }

    @Story("Validation of surface water")
    @Test(groups = "planet", description = "surface_water matches swapi.properties")
    public void verifySurfaceWater() {
        assertEquals(planet.getSurfaceWater(), SwapiProperties.planetsSurfaceWater());
    }

    @Story("Validation of population")
    @Test(groups = "planet", description = "population matches swapi.properties")
    public void verifyPopulation() {
        assertEquals(planet.getPopulation(), SwapiProperties.planetsPopulation());
    }

    @Story("Validation of Pagination: return 10 fields")
    @Test(groups = {"planet", "pagination"}, description = "Default collection page returns 10 records")
    public void verifyDefaultPageSize() {
        Response r = planets.getAll();
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize   = r.jsonPath().getList("results").size();
        int totalCount = r.jsonPath().getInt("count");
        assertEquals(pageSize, 10, "Default page size must be 10 records");
        assertTrue(totalCount > pageSize, "Total count must exceed one page confirming there is pagination");
    }

    @Story("Validation of Pagination: ?limit is not supported")
    @Test(groups = {"planet", "pagination"}, description = "Sending ?limit=5 does not reduce the page size")
    public void verifyCustomLimitBehavior() {
        Response r = planets.getAll(Map.of("limit", 5));
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize = r.jsonPath().getList("results").size();
        assertEquals(pageSize, 10,
                "SWAPI ignores ?limit; the default page size of 10 is always returned");
    }

    @Story("Validation of all fields")
    @Test(groups = "planet", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("name",            equalTo(SwapiProperties.planetsName()))
                .body("rotation_period", equalTo(SwapiProperties.planetsRotationPeriod()))
                .body("orbital_period",  equalTo(SwapiProperties.planetsOrbitalPeriod()))
                .body("diameter",        equalTo(SwapiProperties.planetsDiameter()))
                .body("climate",         equalTo(SwapiProperties.planetsClimate()))
                .body("gravity",         equalTo(SwapiProperties.planetsGravity()))
                .body("terrain",         equalTo(SwapiProperties.planetsTerrain()))
                .body("surface_water",   equalTo(SwapiProperties.planetsSurfaceWater()))
                .body("population",      equalTo(SwapiProperties.planetsPopulation()))
                .body("films",           not(empty()))
                .body("residents",       notNullValue());
    }
}
