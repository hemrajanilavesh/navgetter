package com.investscape.navgetter;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;

import java.util.Arrays;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        if (Arrays.asList(EventType.EXPIRED, EventType.EVICTED, EventType.REMOVED).contains(cacheEvent.getType())) {
            log.info(cacheEvent.getType().name() + " : Value: {}", cacheEvent.getOldValue());
        } else if (Arrays.asList(EventType.UPDATED).contains(cacheEvent.getType())) {
            log.info(cacheEvent.getType().name() + " : Old Value: {}, New Value: {}", cacheEvent.getOldValue(), cacheEvent.getNewValue());
        } else {
            log.info(cacheEvent.getType().name() + " : Value: {}", cacheEvent.getNewValue());
        }
    }
}
