package org.example.issue_coupon.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class RequestUrlUtils {

    public static final String BASE_URL = "http://localhost:8080";
    public static final String EVENTS_COUPONS_ENDPOINT = "/events/coupons/{couponId}";
    public static final String REDIS_COUPONS_ENDPOINT = "/redis/coupons";
    public static final String REDIS_COUPONS_ISSUES_VALIDATE_ENDPOINT = "/redis/coupons-issues/validate";

    public static String buildUri(String endpoint) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(endpoint)
                .encode(UTF_8)
                .toUriString();
    }

    public static String buildUriWithPathVariable(String endpoint, Long pathVariable) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(endpoint)
                .buildAndExpand(pathVariable)
                .encode(UTF_8)
                .toUriString();
    }
}
