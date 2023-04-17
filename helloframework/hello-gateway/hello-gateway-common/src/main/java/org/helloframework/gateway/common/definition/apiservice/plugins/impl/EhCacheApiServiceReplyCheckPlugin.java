package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceReplayCheckPlugin;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by lanjian
 */
public class EhCacheApiServiceReplyCheckPlugin implements ApiServiceReplayCheckPlugin {


    private final static CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache(Constants.REPLY_CACHE_NAME, CacheConfigurationBuilder.
            newCacheConfigurationBuilder(String.class, Long.class, ResourcePoolsBuilder.heap(100))
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(Constants.REPLY_TIMEOUT, TimeUnit.MILLISECONDS)))).build();
    private static Cache<String, Long> reply = null;

    static {
        cacheManager.init();
        reply = cacheManager.getCache(Constants.REPLY_CACHE_NAME, String.class, Long.class);
    }

    public void check(ApiServiceHeader apiServiceHeader, ApiServiceRequest apiServiceRequest) throws ApiException {
/*        long time = apiServiceHeader.getTimestamp();
        if (Math.abs(System.currentTimeMillis() - time) > Constants.REPLAY_TIMEOUT) {
            throw new ApiServiceReplyException(ExceptionCode.REPLAY_CODE);
        }*/
        String nonce = apiServiceRequest.getNonce();
        //tc 在cache里面找得到就不允许访问
        if (StringUtils.isEmpty(nonce) || reply.containsKey(nonce)) {
            throw new ApiException(GateWayCode.REPLAY_CODE);
        } else {
            reply.put(apiServiceRequest.getNonce(), System.currentTimeMillis());
        }
    }
}
