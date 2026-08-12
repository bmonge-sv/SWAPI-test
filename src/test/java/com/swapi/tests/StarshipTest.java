package com.swapi.tests;

import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.swapi.framework.core.BaseEndpoint;
import com.swapi.framework.core.HttpStatus;
import com.swapi.framework.endpoints.StarshipEndpoint;
import com.swapi.framework.models.Starship;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("Starships")
public class StarshipTest extends AbstractResourceTest {

    private final StarshipEndpoint starships = new StarshipEndpoint();
    private Starship starship;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return starships; }
    @Override protected int          validId()      { return 9; } // Death Star
    @Override protected String       resourceName() { return "starships"; }

    /** Fetches starship 9 once; all verify* tests share this cached model. */
    @BeforeClass(groups = "starship")
    public void fetchStarship() {
        response = starships.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "starships/" + validId() + " must return 200 before field assertions can run");
        starship = response.as(Starship.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "starship", description = "GET by id deserializes into the Starship model")
    public void getById_deserializesToStarship() {
        assertNotNull(starship, "Starship model must not be null");
        assertNotNull(starship.getStarshipClass(), "starship_class must be populated");
    }

    @Story("Validation of  starship name")
    @Test(groups = "starship", description = "name matches swapi.properties")
    public void verifyName() {
        assertEquals(starship.getName(), SwapiProperties.starshipsName());
    }

    @Story("Validation of  starship model")
    @Test(groups = "starship", description = "model matches swapi.properties")
    public void verifyModel() {
        assertEquals(starship.getModel(), SwapiProperties.starshipsModel());
    }

    @Story("Validation of  starship manufacturer")
    @Test(groups = "starship", description = "manufacturer matches swapi.properties")
    public void verifyManufacturer() {
        assertEquals(starship.getManufacturer(), SwapiProperties.starshipsManufacturer());
    }

    @Story("Validation of  starship cost in credits")
    @Test(groups = "starship", description = "cost_in_credits matches swapi.properties")
    public void verifyCostInCredits() {
        assertEquals(starship.getCostInCredits(), SwapiProperties.starshipsCostInCredits());
    }

    @Story("Validation of  starship length")
    @Test(groups = "starship", description = "length matches swapi.properties")
    public void verifyLength() {
        assertEquals(starship.getLength(), SwapiProperties.starshipsLength());
    }

    @Story("Validation of  starship max atmosphering speed")
    @Test(groups = "starship", description = "max_atmosphering_speed matches swapi.properties")
    public void verifyMaxAtmospheringSpeed() {
        assertEquals(starship.getMaxAtmospheringSpeed(), SwapiProperties.starshipsMaxAtmospheringSpeed());
    }

    @Story("Validation of  starship crew")
    @Test(groups = "starship", description = "crew matches swapi.properties")
    public void verifyCrew() {
        assertEquals(starship.getCrew(), SwapiProperties.starshipsCrew());
    }

    @Story("Validation of  starship passengers")
    @Test(groups = "starship", description = "passengers matches swapi.properties")
    public void verifyPassengers() {
        assertEquals(starship.getPassengers(), SwapiProperties.starshipsPassengers());
    }

    @Story("Validation of  starship cargo capacity")
    @Test(groups = "starship", description = "cargo_capacity matches swapi.properties")
    public void verifyCargoCapacity() {
        assertEquals(starship.getCargoCapacity(), SwapiProperties.starshipsCargoCapacity());
    }

    @Story("Validation of Pagination: return 10 fields")
    @Test(groups = {"starship", "pagination"}, description = "Default collection page returns 10 records")
    public void verifyDefaultPageSize() {
        Response r = starships.getAll();
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize   = r.jsonPath().getList("results").size();
        int totalCount = r.jsonPath().getInt("count");
        assertEquals(pageSize, 10, "Default page size must be 10 records");
        assertTrue(totalCount > pageSize, "Total count must exceed one page confirming there is pagination");
    }

    @Story("Validation of Pagination: ?limit is not supported")
    @Test(groups = {"starship", "pagination"}, description = "Sending ?limit=5 does not reduce the page size")
    public void verifyCustomLimitBehavior() {
        Response r = starships.getAll(Map.of("limit", 5));
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize = r.jsonPath().getList("results").size();
        assertEquals(pageSize, 10,
                "SWAPI ignores ?limit; the default page size of 10 is always returned");
    }

    @Story("Validation of all fields")
    @Test(groups = "starship", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("name",                   equalTo(SwapiProperties.starshipsName()))
                .body("model",                  equalTo(SwapiProperties.starshipsModel()))
                .body("manufacturer",           equalTo(SwapiProperties.starshipsManufacturer()))
                .body("cost_in_credits",        equalTo(SwapiProperties.starshipsCostInCredits()))
                .body("length",                 equalTo(SwapiProperties.starshipsLength()))
                .body("max_atmosphering_speed", equalTo(SwapiProperties.starshipsMaxAtmospheringSpeed()))
                .body("crew",                   equalTo(SwapiProperties.starshipsCrew()))
                .body("passengers",             equalTo(SwapiProperties.starshipsPassengers()))
                .body("cargo_capacity",         equalTo(SwapiProperties.starshipsCargoCapacity()))
                .body("starship_class",         notNullValue())
                .body("films",                  not(empty()));
    }
}
