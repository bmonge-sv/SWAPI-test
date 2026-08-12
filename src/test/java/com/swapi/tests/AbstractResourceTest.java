package com.swapi.tests;

import com.swapi.framework.core.BaseEndpoint;
import com.swapi.framework.core.HttpStatus;
import com.swapi.framework.reporting.PerformanceRating;
import com.swapi.framework.reporting.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Verb-level contract every SWAPI resource must honour. Each concrete resource
 * test supplies its endpoint object and a known-good id; the shared cases below
 * then run against that resource.
 *
 * <p>Grouped as {@code happy} (GET/HEAD/OPTIONS succeed) and {@code negative}
 * (POST/PUT/DELETE are rejected because the API is read-only), so either slice
 * can be run on its own.</p>
 */
public abstract class AbstractResourceTest extends BaseTest {

    /** The endpoint object under test. */
protected abstract BaseEndpoint endpoint();

    /** An id that is known to exist for this resource. */
protected abstract int validId();

    /** Human-readable resource name, used in assertion messages. */
protected abstract String resourceName();

    // ------------------------------------------------------------------
    // Happy path — read verbs
    // ------------------------------------------------------------------

@Story("GET collection → 200 with paged results")
@Test(groups = "happy", description = "GET collection returns 200 with a non-empty, paged result set")
public void getCollection_returnsOkAndResults() {
        Response response = endpoint().getAll();

        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                resourceName() + " collection should return 200");
        assertTrue(response.jsonPath().getInt("count") > 0,
                resourceName() + " count should be positive");
        assertFalse(response.jsonPath().getList("results").isEmpty(),
                resourceName() + " results page should not be empty");
}

@Story("GET /{id} → 200 with url field")
@Test(groups = "happy", description = "GET by id returns 200 and a hypermedia url")
public void getById_returnsOk() {
        Response response = endpoint().getById(validId());

        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                resourceName() + "/" + validId() + " should return 200");
        assertNotNull(response.jsonPath().getString("url"),
                resourceName() + " item should expose a url");
}

@Story("HEAD → 200 no body")
@Test(groups = "happy", description = "HEAD returns 200 and no body")
public void head_returnsOkWithNoBody() {
        Response response = endpoint().head();

        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "HEAD " + resourceName() + " should return 200");
        assertTrue(response.getBody().asString().isEmpty(),
                "HEAD response must not carry a body");
}

@Story("OPTIONS → 200")
@Test(groups = "happy", description = "OPTIONS returns 200 describing the resource")
public void options_returnsOk() {
        Response response = endpoint().options();

        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "OPTIONS " + resourceName() + " should return 200");
}

@Story("GET /{id} → body has url, created, edited")
@Test(groups = "happy", description = "GET by id body exposes common BaseModel fields via Hamcrest matchers")
public void getById_bodyHasCommonFields() {
        endpoint().getById(validId())
                .then()
                .statusCode(HttpStatus.OK.code())
                .body("url",     notNullValue())
                .body("created", notNullValue())
                .body("edited",  notNullValue());
}

    // ------------------------------------------------------------------
    // Negative path — mutation verbs must be rejected (read-only API)
    // ------------------------------------------------------------------

@Story("POST → 405 Method Not Allowed")
@Test(groups = "negative", description = "POST is rejected on a read-only API")
public void post_isRejected() {
        Response response = endpoint().post(Map.of("name", "should-not-be-created"));

        assertEquals(response.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED.code(),
                "POST " + resourceName() + " must return 405 on a read-only API");
}

@Story("PUT → 405 Method Not Allowed")
@Test(groups = "negative", description = "PUT is rejected on a read-only API")
public void put_isRejected() {
        Response response = endpoint().put(validId(), Map.of("name", "should-not-be-updated"));

        assertEquals(response.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED.code(),
                "PUT " + resourceName() + " must return 405 on a read-only API");
}

@Story("DELETE → 405 Method Not Allowed")
@Test(groups = "negative", description = "DELETE is rejected on a read-only API")
public void delete_isRejected() {
        Response response = endpoint().delete(validId());

        assertEquals(response.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED.code(),
                "DELETE " + resourceName() + " must return 405 on a read-only API");
}

    // ------------------------------------------------------------------
    // Security — XSS injection probe
    // ------------------------------------------------------------------

    @Story("XSS injection in path → 404")
    @Test(groups = "security", description = "GET /<script> returns 404 — server must not process the injection payload")
    public void getByXssPayload_returns404() {
        Response response = endpoint().getByPath("<script>");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND.code(),
                resourceName() + "/<script> must return 404 — payload must not be processed or reflected");
    }

    // ------------------------------------------------------------------
    // Performance — response time must not exceed the Lento threshold
    // ------------------------------------------------------------------

@Story("Tiempo de respuesta de la colección")
@Test(groups = "performance", description = "GET collection response time rated Excelente/Bueno/Aceptable — must not be Lento (>2 s)")
public void verifyResponseTime() {
        Response response = endpoint().getAll();
        long ms     = response.getTime();
        String rating = PerformanceRating.classify(ms);
        assertTrue(ms <= PerformanceRating.ACCEPTABLE_MS,
                resourceName() + " collection response time " + ms + " ms is rated '"
                + rating + "' — exceeds the 2 s Lento threshold");
}
}
