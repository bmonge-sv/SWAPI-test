package com.swapi.tests;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

/**
 * Global test setup. Runs once per test class and configures RestAssured
 * defaults that apply to the whole suite.
 */
public abstract class BaseTest {

    @BeforeClass(alwaysRun = true)
    public void globalSetup() {
        configureProxy();
        // Tolerate certificate quirks on public mirrors.
        RestAssured.useRelaxedHTTPSValidation();
        // On any failed assertion, dump the full request/response for triage.
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * Reads the proxy URL from the standard POSIX environment variables in the
     * same priority order that curl uses, so the suite routes requests through
     * the corporate proxy when one is configured on the machine.
     */
    private void configureProxy() {
        String proxyUrl = resolveProxyEnv();
        if (proxyUrl == null) return;
        try {
            URI uri = new URI(proxyUrl);
            int port = uri.getPort() == -1 ? 8080 : uri.getPort();
            RestAssured.proxy(uri.getHost(), port);
        } catch (URISyntaxException e) {
            System.err.printf("[BaseTest] Could not configure proxy from '%s': %s%n", proxyUrl, e.getMessage());
        }
    }

    private static String resolveProxyEnv() {
        for (String key : new String[]{"https_proxy", "HTTPS_PROXY", "http_proxy", "HTTP_PROXY"}) {
            String val = System.getenv(key);
            if (val != null && !val.isBlank()) return val;
        }
        return null;
    }
}
