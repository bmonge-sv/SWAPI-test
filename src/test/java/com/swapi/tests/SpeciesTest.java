package com.swapi.tests;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.swapi.framework.core.BaseEndpoint;
import com.swapi.framework.core.HttpStatus;
import com.swapi.framework.endpoints.SpeciesEndpoint;
import com.swapi.framework.models.Species;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("Species")
public class SpeciesTest extends AbstractResourceTest {

    private final SpeciesEndpoint species = new SpeciesEndpoint();
    private Species result;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return species; }
    @Override protected int          validId()      { return 1; } // Human
    @Override protected String       resourceName() { return "species"; }

    /** Fetches species 1 once; all verify* tests share this cached model. */
    @BeforeClass(groups = "specie")
    public void fetchSpecies() {
        response = species.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "species/" + validId() + " must return 200 before field assertions can run");
        result = response.as(Species.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "specie", description = "GET by id deserializes into the Species model")
    public void getById_deserializesToSpecies() {
        assertNotNull(result, "Species model must not be null");
    }

    @Story("Validation of species name")
    @Test(groups = "specie", description = "name matches swapi.properties")
    public void verifyName() {
        assertEquals(result.getName(), SwapiProperties.speciesName());
    }

    @Story("Validation of species classification")
    @Test(groups = "specie", description = "classification matches swapi.properties")
    public void verifyClassification() {
        assertEquals(result.getClassification(), SwapiProperties.speciesClassification());
    }

    @Story("Validation of species designation")
    @Test(groups = "specie", description = "designation matches swapi.properties")
    public void verifyDesignation() {
        assertEquals(result.getDesignation(), SwapiProperties.speciesDesignation());
    }

    @Story("Validation of species average height")
    @Test(groups = "specie", description = "average_height matches swapi.properties")
    public void verifyAverageHeight() {
        assertEquals(result.getAverageHeight(), SwapiProperties.speciesAverageHeight());
    }

    @Story("Validation of species skin colors")
    @Test(groups = "specie", description = "skin_colors matches swapi.properties")
    public void verifySkinColors() {
        assertEquals(result.getSkinColors(), SwapiProperties.speciesSkinColors());
    }

    @Story("Validation of species hair colors")
    @Test(groups = "specie", description = "hair_colors matches swapi.properties")
    public void verifyHairColors() {
        assertEquals(result.getHairColors(), SwapiProperties.speciesHairColors());
    }

    @Story("VAlidation of species eye colors")
    @Test(groups = "specie", description = "eye_colors matches swapi.properties")
    public void verifyEyeColors() {
        assertEquals(result.getEyeColors(), SwapiProperties.speciesEyeColors());
    }

    @Story("Validation of species average lifespan")
    @Test(groups = "specie", description = "average_lifespan matches swapi.properties")
    public void verifyAverageLifespan() {
        assertEquals(result.getAverageLifespan(), SwapiProperties.speciesAverageLifespan());
    }

    @Story("Validation of species language")
    @Test(groups = "specie", description = "language matches swapi.properties")
    public void verifyLanguage() {
        assertEquals(result.getLanguage(), SwapiProperties.speciesLanguage());
    }

    @Story("Validation of all fields")
    @Test(groups = "specie", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("name",             equalTo(SwapiProperties.speciesName()))
                .body("classification",   equalTo(SwapiProperties.speciesClassification()))
                .body("designation",      equalTo(SwapiProperties.speciesDesignation()))
                .body("average_height",   equalTo(SwapiProperties.speciesAverageHeight()))
                .body("skin_colors",      equalTo(SwapiProperties.speciesSkinColors()))
                .body("hair_colors",      equalTo(SwapiProperties.speciesHairColors()))
                .body("eye_colors",       equalTo(SwapiProperties.speciesEyeColors()))
                .body("average_lifespan", equalTo(SwapiProperties.speciesAverageLifespan()))
                .body("language",         equalTo(SwapiProperties.speciesLanguage()))
                .body("people",           not(empty()))
                .body("homeworld",        notNullValue());
    }
}
