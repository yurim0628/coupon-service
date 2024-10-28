package org.example.redis.service;

import org.springframework.data.redis.core.SessionCallback;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    <T> Optional<T> get(String key, Class<T> type);

    void set(String key, Object value, Long expiredTime);

    boolean setIfAbsent(String key, Object value);

    boolean delete(String key);

    void expire(String key, long timeout, TimeUnit unit);

    <T> void execute(SessionCallback<T> sessionCallback);
}
