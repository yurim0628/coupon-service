package org.example.redis.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.CommonException;
import org.example.redis.domain.CouponRedis;
import org.example.redis.infrastructure.entity.CouponRedisEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.common.exception.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisCache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis에서 특정 키로 저장된 쿠폰 데이터를 조회하는 메서드.
     * 키에 해당하는 데이터가 없거나 JSON 변환에 실패할 경우 빈 Optional을 반환.
     *
     * @param key Redis에 저장된 쿠폰 데이터의 키.
     * @return 조회된 쿠폰 데이터를 포함하는 Optional<CouponRedisEntity>.
     * @throws CommonException Redis에서 데이터 조회 중 예외 발생 시 공통 예외로 처리.
     */
    public Optional<CouponRedisEntity> getCoupon(String key) {
        log.info("Find Redis Key = [{}]", key);
        String serializedValue = redisTemplate.opsForValue().get(key);
        try {
            return Optional.ofNullable(objectMapper.readValue(serializedValue, CouponRedisEntity.class));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            log.error("Failed to Deserialize Coupon", e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception", e);
            throw new CommonException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * 쿠폰 객체를 Redis에 저장하는 메서드.
     * 주어진 쿠폰 객체를 CouponRedisEntity로 변환하여 직렬화 후 Redis에 저장.
     *
     * @param key Redis에 저장될 쿠폰 데이터의 키.
     * @param coupon Redis에 저장할 쿠폰 도메인 객체.
     * @throws CommonException 직렬화 또는 Redis 저장 중 예외 발생 시 공통 예외로 처리.
     */
    public void setCoupon(String key, CouponRedis coupon) {
        log.info("Save Redis Key = [{}], Value = [{}]", key, coupon);
        try {
            CouponRedisEntity couponRedisEntity = CouponRedisEntity.fromModel(coupon);
            String serializedValue = objectMapper.writeValueAsString(couponRedisEntity);
            redisTemplate.opsForValue().set(key, serializedValue);
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CommonException("Redis set() Error", COMMON_SYSTEM_ERROR);
        }
    }
}
