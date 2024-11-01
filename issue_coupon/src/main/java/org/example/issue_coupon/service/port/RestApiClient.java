package org.example.issue_coupon.service.port;

import java.util.Optional;

public interface RestApiClient {

    <T> Optional<T> get(String url, Class<T> type);

    <T, R> void post(String url, R request, Class<T> type);
}
