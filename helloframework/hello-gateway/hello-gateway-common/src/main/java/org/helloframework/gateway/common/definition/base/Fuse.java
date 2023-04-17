package org.helloframework.gateway.common.definition.base;

import org.helloframework.gateway.common.exception.FuseException;

/**
 * Created by lanjian
 */
public interface Fuse {
    void fuse(ServerInfo serverInfo) throws FuseException;
}
