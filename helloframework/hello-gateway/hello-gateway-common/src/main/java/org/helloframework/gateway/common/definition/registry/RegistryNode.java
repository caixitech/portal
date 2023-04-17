
package org.helloframework.gateway.common.definition.registry;


import org.helloframework.gateway.common.utils.URL;

/**
 * Node. (API/SPI, Prototype, ThreadSafe)
 */
public interface RegistryNode {

    /**
     * get url.
     *
     * @return url.
     */
    URL getUrl();

    /**
     * is available.
     *
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();

}