package com.swapi.framework.core;

import com.swapi.framework.constants.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Shared behaviour for every resource endpoint. Each concrete subclass supplies
 * its collection path (e.g. {@code /planets/}) and inherits the full verb set.
 *
 * <p>The three read verbs ({@link #getAll()}, {@link #getById(int)}, {@link #head()},
 * {@link #options()}) are the happy path. The three write verbs
 * ({@link #post(Object)}, {@link #put(int, Object)}, {@link #delete(int)}) exist
 * purely so negative tests can prove the API rejects mutation. Endpoints return
 * the raw {@link Response}; assertions live in the test layer.</p>
 */
public abstract class BaseEndpoint {

    protected final String collectionPath;

    protected BaseEndpoint(String collectionPath) {
        this.collectionPath = collectionPath;
    }

    private RequestSpecification request() {
        return RestAssured.given().spec(SpecFactory.requestSpec());
    }

    // ---------------------------------------------------------------------
    // Happy path: read-only verbs
    // ---------------------------------------------------------------------

    /** GET the full collection (paged). */
    public Response getAll() {
        Response r = request().when().get(collectionPath);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** GET the full collection with additional query parameters (e.g. {@code page}, {@code limit}). */
    public Response getAll(Map<String, Object> queryParams) {
        Response r = request().queryParams(queryParams).when().get(collectionPath);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** GET a single resource by id. */
    public Response getById(int id) {
        Response r = request().pathParam("id", id).when().get(collectionPath + Endpoints.BY_ID);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /**
     * GET with an arbitrary path segment appended to the collection path.
     * The segment is passed as a RestAssured path parameter so special characters
     * (e.g. {@code <script>}) are URL-encoded before the request is sent.
     * Used exclusively by security tests that probe injection payloads.
     */
    public Response getByPath(String segment) {
        Response r = request().pathParam("segment", segment)
                              .when().get(collectionPath + "{segment}");
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** HEAD on the collection — same headers as GET, no body. */
    public Response head() {
        Response r = request().when().head(collectionPath);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** OPTIONS on the collection — advertises supported methods/metadata. */
    public Response options() {
        Response r = request().when().options(collectionPath);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    // ---------------------------------------------------------------------
    // Negative path: mutation verbs (expected to be rejected)
    // ---------------------------------------------------------------------

    /** POST to the collection — should be refused on a read-only API. */
    public Response post(Object body) {
        Response r = request().body(body).when().post(collectionPath);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** PUT on a single resource — should be refused on a read-only API. */
    public Response put(int id, Object body) {
        Response r = request().pathParam("id", id).body(body).when().put(collectionPath + Endpoints.BY_ID);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }

    /** DELETE a single resource — should be refused on a read-only API. */
    public Response delete(int id) {
        Response r = request().pathParam("id", id).when().delete(collectionPath + Endpoints.BY_ID);
        ResponseCapture.setStatusCode(r.getStatusCode());
        return r;
    }
}
