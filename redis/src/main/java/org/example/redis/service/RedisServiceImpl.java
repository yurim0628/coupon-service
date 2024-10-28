package org.example.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.CommonException;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.example.common.exception.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        log.info("Find Redis Key = [{}], Type = [{}]", key, type.getName());
        String serializedValue = redisTemplate.opsForValue().get(key);

        try {
            return Optional.of(objectMapper.readValue(serializedValue, type));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception", e);
            throw new CommonException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public void set(String key, Object value, Long expiredTime) {
        log.info("Save Redis Key = [{}], Value = [{}], expiredTime = [{}]", key, value, expiredTime);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, serializedValue, expiredTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CommonException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public boolean setIfAbsent(String key, Object value) {
        log.info("Save If Absent Redis Key = [{}], Value = [{}]", key, value);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            return Boolean.TRUE.equals(
                    redisTemplate.opsForValue().setIfAbsent(
                            key,
                            serializedValue
                    ));
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CommonException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public boolean delete(String key) {
        log.info("Delete Redis Key = [{}]", key);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        log.info("Setting expiration for Redis Key = [{}], Timeout = [{}] {},", key, timeout, unit);
        try {
            boolean result = Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
            if (!result) {
                log.warn("Failed to set expiration for Redis Key = [{}]", key);
            }
        } catch (Exception e) {
            log.error("Redis Expire Exception for Key = [{}]", key, e);
            throw new CommonException("Failed to set expiration for key: " + key, COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public <T> void execute(SessionCallback<T> sessionCallback) {
        log.info("Executing Redis session callback");
        try {
            redisTemplate.execute(sessionCallback);
            log.info("Redis session callback executed successfully");
        } catch (Exception e) {
            log.error("Redis Session Callback Exception", e);
            throw new CommonException("Failed to execute Redis session callback", COMMON_SYSTEM_ERROR);
        }
    }
}
