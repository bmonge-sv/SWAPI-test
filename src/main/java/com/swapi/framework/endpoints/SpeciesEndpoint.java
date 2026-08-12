package com.swapi.framework.endpoints;

import com.swapi.framework.constants.Endpoints;
import com.swapi.framework.core.BaseEndpoint;

/**
 * Endpoint object for the {@code species} resource.
 * Inherits the full verb set (GET/HEAD/OPTIONS + POST/PUT/DELETE) from {@link BaseEndpoint}.
 */
public class SpeciesEndpoint extends BaseEndpoint {

    public SpeciesEndpoint() {
        super(Endpoints.SPECIES);
    }
}
