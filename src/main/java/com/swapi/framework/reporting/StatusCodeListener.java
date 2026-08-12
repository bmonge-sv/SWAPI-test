package com.swapi.framework.reporting;

import com.swapi.framework.core.ResponseCapture;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class StatusCodeListener implements IInvokedMethodListener {

    /**
     * Holds the status code captured by the most recent @BeforeClass HTTP call,
     * per thread. Used as a fallback for @Test methods that do not make their
     * own HTTP request (e.g. model-assertion tests that rely on a cached response).
     */
    private static final ThreadLocal<Integer> SETUP_CODE = new ThreadLocal<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        ResponseCapture.clear();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Integer code = ResponseCapture.getStatusCode();

        if (method.isConfigurationMethod()) {
            // Persist the @BeforeClass status code so @Test methods can fall back to it
            if (code != null) SETUP_CODE.set(code);
            return;
        }

        if (!method.isTestMethod()) return;

        // @Test that made its own HTTP call wins; otherwise use @BeforeClass code
        if (code == null) code = SETUP_CODE.get();
        if (code != null) testResult.setAttribute("statusCode", code);
    }
}
