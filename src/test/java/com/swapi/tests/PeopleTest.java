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
import com.swapi.framework.endpoints.PeopleEndpoint;
import com.swapi.framework.models.Person;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("People")
public class PeopleTest extends AbstractResourceTest {

    private final PeopleEndpoint people = new PeopleEndpoint();
    private Person person;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return people; }
    @Override protected int          validId()      { return 1; }
    @Override protected String       resourceName() { return "people"; }

    
    @BeforeClass(groups = "people")
    public void fetchPerson() {
        response = people.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "people/" + validId() + " must return 200 before field assertions can run");
        person = response.as(Person.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "people", description = "GET by id deserializes into the Person model")
    public void getById_deserializesToPerson() {
        assertNotNull(person, "Person model must not be null");
        assertNotNull(person.getHomeworld(), "homeworld must be populated");
    }

    @Story("Validation of character name")
    @Test(groups = "people", description = "name matches swapi.properties")
    public void verifyName() {
        assertEquals(person.getName(), SwapiProperties.peopleName());
    }

    @Story("Validation of character height")
    @Test(groups = "people", description = "height matches swapi.properties")
    public void verifyHeight() {
        assertEquals(person.getHeight(), SwapiProperties.peopleHeight());
    }

    @Story("Validation of character mass")
    @Test(groups = "people", description = "mass matches swapi.properties")
    public void verifyMass() {
        assertEquals(person.getMass(), SwapiProperties.peopleMass());
    }

    @Story("Validation of character hair color")
    @Test(groups = "people", description = "hair_color matches swapi.properties")
    public void verifyHairColor() {
        assertEquals(person.getHairColor(), SwapiProperties.peopleHairColor());
    }

    @Story("Validation of character skin color")
    @Test(groups = "people", description = "skin_color matches swapi.properties")
    public void verifySkinColor() {
        assertEquals(person.getSkinColor(), SwapiProperties.peopleSkinColor());
    }

    @Story("Validation of character eye color")
    @Test(groups = "people", description = "eye_color matches swapi.properties")
    public void verifyEyeColor() {
        assertEquals(person.getEyeColor(), SwapiProperties.peopleEyeColor());
    }

    @Story("Validation of character birth year")
    @Test(groups = "people", description = "birth_year matches swapi.properties")
    public void verifyBirthYear() {
        assertEquals(person.getBirthYear(), SwapiProperties.peopleBirthYear());
    }

    @Story("Validation of character gender")
    @Test(groups = "people", description = "gender matches swapi.properties")
    public void verifyGender() {
        assertEquals(person.getGender(), SwapiProperties.peopleGender());
    }

    @Story("Validation of Pagination: return 10 fields")
    @Test(groups = {"people", "pagination"}, description = "Default collection page returns 10 records")
    public void verifyDefaultPageSize() {
        Response r = people.getAll();
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize  = r.jsonPath().getList("results").size();
        int totalCount = r.jsonPath().getInt("count");
        assertEquals(pageSize, 10, "Default page size must be 10 records");
        assertTrue(totalCount > pageSize, "Total count must exceed one page confirming there is pagination");
    }

    @Story("Validation of Pagination: ?limit is not supported")
    @Test(groups = {"people", "pagination"}, description = "Sending ?limit=5 does not reduce the page size")
    public void verifyCustomLimitBehavior() {
        Response r = people.getAll(Map.of("limit", 5));
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize = r.jsonPath().getList("results").size();
        assertEquals(pageSize, 10,
                "SWAPI ignores ?limit; the default page size of 10 is always returned");
    }

    @Story("Validation of all fields")
    @Test(groups = "people", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("name",       equalTo(SwapiProperties.peopleName()))
                .body("height",     equalTo(SwapiProperties.peopleHeight()))
                .body("mass",       equalTo(SwapiProperties.peopleMass()))
                .body("hair_color", equalTo(SwapiProperties.peopleHairColor()))
                .body("skin_color", equalTo(SwapiProperties.peopleSkinColor()))
                .body("eye_color",  equalTo(SwapiProperties.peopleEyeColor()))
                .body("birth_year", equalTo(SwapiProperties.peopleBirthYear()))
                .body("gender",     equalTo(SwapiProperties.peopleGender()))
                .body("homeworld",  notNullValue())
                .body("films",      not(empty()));
    }
}
