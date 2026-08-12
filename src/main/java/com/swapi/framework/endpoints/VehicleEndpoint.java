package com.swapi.framework.endpoints;

import com.swapi.framework.constants.Endpoints;
import com.swapi.framework.core.BaseEndpoint;

/**
 * Inherits the full verb set (GET/HEAD/OPTIONS + POST/PUT/DELETE) from {@link BaseEndpoint}.
 */
public class VehicleEndpoint extends BaseEndpoint {

    public VehicleEndpoint() {
        super(Endpoints.VEHICLES);
    }
}
