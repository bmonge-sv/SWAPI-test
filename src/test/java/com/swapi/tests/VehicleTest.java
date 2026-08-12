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
import com.swapi.framework.endpoints.VehicleEndpoint;
import com.swapi.framework.models.Vehicle;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("Vehicles")
public class VehicleTest extends AbstractResourceTest {

    private final VehicleEndpoint vehicles = new VehicleEndpoint();
    private Vehicle vehicle;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return vehicles; }
    @Override protected int          validId()      { return 4; }
    @Override protected String       resourceName() { return "vehicles"; }

    /** Fetches vehicle 4 once; all verify **/
    @BeforeClass(groups = "vehicle")
    public void fetchVehicle() {
        response = vehicles.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "vehicles/" + validId() + " must return 200 before field assertions can run");
        vehicle = response.as(Vehicle.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "vehicle", description = "GET by id deserializes into the Vehicle model")
    public void getById_deserializesToVehicle() {
        assertNotNull(vehicle, "Vehicle model must not be null");
        assertNotNull(vehicle.getVehicleClass(), "vehicle_class must be populated");
    }

    @Story("Validation of  vehicle name")
    @Test(groups = "vehicle", description = "name matches swapi.properties")
    public void verifyName() {
        assertEquals(vehicle.getName(), SwapiProperties.vehiclesName());
    }

    @Story("Validation of  vehicle model")
    @Test(groups = "vehicle", description = "model matches swapi.properties")
    public void verifyModel() {
        assertEquals(vehicle.getModel(), SwapiProperties.vehiclesModel());
    }

    @Story("Validation of  vehicle manufacturer")
    @Test(groups = "vehicle", description = "manufacturer matches swapi.properties")
    public void verifyManufacturer() {
        assertEquals(vehicle.getManufacturer(), SwapiProperties.vehiclesManufacturer());
    }

    @Story("Validation of  vehicle cost in credits")
    @Test(groups = "vehicle", description = "cost_in_credits matches swapi.properties")
    public void verifyCostInCredits() {
        assertEquals(vehicle.getCostInCredits(), SwapiProperties.vehiclesCostInCredits());
    }

    @Story("Validation of  vehicle length")
    @Test(groups = "vehicle", description = "length matches swapi.properties")
    public void verifyLength() {
        assertEquals(vehicle.getLength(), SwapiProperties.vehiclesLength());
    }

    @Story("Validation of  vehicle max atmosphering speed")
    @Test(groups = "vehicle", description = "max_atmosphering_speed matches swapi.properties")
    public void verifyMaxAtmospheringSpeed() {
        assertEquals(vehicle.getMaxAtmospheringSpeed(), SwapiProperties.vehiclesMaxAtmospheringSpeed());
    }

    @Story("Validation of  vehicle crew")
    @Test(groups = "vehicle", description = "crew matches swapi.properties")
    public void verifyCrew() {
        assertEquals(vehicle.getCrew(), SwapiProperties.vehiclesCrew());
    }

    @Story("Validation of  vehicle passengers")
    @Test(groups = "vehicle", description = "passengers matches swapi.properties")
    public void verifyPassengers() {
        assertEquals(vehicle.getPassengers(), SwapiProperties.vehiclesPassengers());
    }

    @Story("Validation of Pagination: return 10 fields")
    @Test(groups = {"vehicle", "pagination"}, description = "Default collection page returns 10 records")
    public void verifyDefaultPageSize() {
        Response r = vehicles.getAll();
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize   = r.jsonPath().getList("results").size();
        int totalCount = r.jsonPath().getInt("count");
        assertEquals(pageSize, 10, "Default page size must be 10 records");
        assertTrue(totalCount > pageSize, "Total count must exceed one page confirming there is pagination");
    }

    @Story("Validation of Pagination: ?limit is not supported")
    @Test(groups = {"vehicle", "pagination"}, description = "Sending ?limit=5 does not reduce the page size")
    public void verifyCustomLimitBehavior() {
        Response r = vehicles.getAll(Map.of("limit", 5));
        assertEquals(r.getStatusCode(), HttpStatus.OK.code());
        int pageSize = r.jsonPath().getList("results").size();
        assertEquals(pageSize, 10,
                "SWAPI ignores ?limit; the default page size of 10 is always returned");
    }

    @Story("Validation of all fields")
    @Test(groups = "vehicle", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("name",                   equalTo(SwapiProperties.vehiclesName()))
                .body("model",                  equalTo(SwapiProperties.vehiclesModel()))
                .body("manufacturer",           equalTo(SwapiProperties.vehiclesManufacturer()))
                .body("cost_in_credits",        equalTo(SwapiProperties.vehiclesCostInCredits()))
                .body("length",                 equalTo(SwapiProperties.vehiclesLength()))
                .body("max_atmosphering_speed", equalTo(SwapiProperties.vehiclesMaxAtmospheringSpeed()))
                .body("crew",                   equalTo(SwapiProperties.vehiclesCrew()))
                .body("passengers",             equalTo(SwapiProperties.vehiclesPassengers()))
                .body("vehicle_class",          notNullValue())
                .body("films",                  not(empty()));
    }
}
