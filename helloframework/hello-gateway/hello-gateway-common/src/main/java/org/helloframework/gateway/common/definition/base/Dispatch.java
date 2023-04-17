package org.helloframework.gateway.common.definition.base;

import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;

/**
 * Created by lanjian
 */
public interface Dispatch {
    Object dispatch(ApiServiceRequest apiServiceRequest);

    ClientWarp randomClient(ApiServiceRequest apiServiceRequest);
}
