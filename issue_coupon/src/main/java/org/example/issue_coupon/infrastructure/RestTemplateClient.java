package org.example.issue_coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.CommonException;
import org.example.issue_coupon.service.port.RestApiClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.example.common.exception.ErrorCode.COMMON_SYSTEM_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateClient implements RestApiClient {

    private final RestTemplate restTemplate;

    @Override
    public <T> Optional<T> get(String url, Class<T> type) {
        try{
            return Optional.ofNullable(restTemplate.getForObject(url, type));
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Get Exception Message = {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("RestTemplate Get Exception", e);
            throw new CommonException(COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public <T, R> void post(String url, R request, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<R> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForObject(url, entity, type);
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Post Exception Message = {}", e.getMessage());
        } catch (Exception e) {
            log.error("RestTemplate Post Exception", e);
            throw new CommonException(COMMON_SYSTEM_ERROR);
        }
    }
}
