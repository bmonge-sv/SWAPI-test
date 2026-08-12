package com.swapi.framework.core;

import com.swapi.framework.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Single source of truth for how a request is built: base URI, base path,
 * default content type and request logging. Endpoint objects call this instead
 * of repeating {@code given()} boilerplate, so a change here (say, adding an
 * auth header) applies to the whole suite at once.
 */
public final class SpecFactory {

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int SOCKET_TIMEOUT_MS  = 30_000;

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(ConfigManager.getBaseUri())
            .setBasePath(ConfigManager.getBasePath())
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .setConfig(RestAssuredConfig.newConfig()
                    .httpClient(HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", CONNECT_TIMEOUT_MS)
                            .setParam("http.socket.timeout",     SOCKET_TIMEOUT_MS)))
            .log(LogDetail.METHOD)
            .log(LogDetail.URI)
            .build();

    private SpecFactory() {
        // Utility class; no instances.
    }

    public static RequestSpecification requestSpec() {
        return REQUEST_SPEC;
    }
}
