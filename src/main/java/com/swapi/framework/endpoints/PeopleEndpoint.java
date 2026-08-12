package com.swapi.framework.endpoints;

import com.swapi.framework.constants.Endpoints;
import com.swapi.framework.core.BaseEndpoint;

/**
 * Endpoint object for the {@code people} resource.
 * Inherits the full verb set (GET/HEAD/OPTIONS + POST/PUT/DELETE) from {@link BaseEndpoint}.
 */
public class PeopleEndpoint extends BaseEndpoint {

    public PeopleEndpoint() {
        super(Endpoints.PEOPLE);
    }
}
