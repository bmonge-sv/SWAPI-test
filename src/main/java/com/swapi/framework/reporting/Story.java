package com.swapi.framework.reporting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Human-readable label shown in the HTML report instead of the class or method name.
 *
 * <p>On a <b>class</b>: replaces the resource prefix (left side of "::") in the Endpoint column.
 * On a <b>method</b>: replaces the method name (right side of "::").
 * Both can be combined — class sets the resource name, method sets the scenario description.</p>
 *
 * <pre>
 * {@literal @}Story("People")
 * public class PeopleTest { ... }
 *
 * {@literal @}Story("name matches expected")
 * {@literal @}Test public void verifyName() { ... }
 * // → report shows: "People :: name matches expected"
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Story {
    String value();
}
