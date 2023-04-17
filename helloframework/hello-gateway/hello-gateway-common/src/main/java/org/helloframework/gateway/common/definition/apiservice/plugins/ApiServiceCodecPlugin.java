package org.helloframework.gateway.common.definition.apiservice.plugins;

/**
 * Created by lanjian on 23/08/2017.
 */
public interface ApiServiceCodecPlugin<T, K> extends Plugin {
    byte[] encode(T t, Class cls);

    K decode(byte[] in, Class cls);
}
