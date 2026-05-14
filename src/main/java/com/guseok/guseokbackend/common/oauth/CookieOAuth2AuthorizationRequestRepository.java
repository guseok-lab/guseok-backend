package com.guseok.guseokbackend.common.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
public class CookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String COOKIE_NAME = "oauth2_auth_request";
    private static final int COOKIE_MAX_AGE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return getCookieValue(request, COOKIE_NAME)
            .map(value -> {
                OAuth2AuthorizationRequest authRequest = deserialize(value);
                if (authRequest == null) {
                    log.warn("oauth2_auth_request 쿠키 역직렬화 실패 (쿠키값 손상 또는 만료)");
                }
                return authRequest;
            })
            .orElseGet(() -> {
                log.debug("oauth2_auth_request 쿠키 없음");
                return null;
            });
    }

    @Override
    public void saveAuthorizationRequest(
        OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        if (authorizationRequest == null) {
            deleteCookie(response);
            return;
        }
        // ResponseCookie로 SameSite=Lax 명시 설정
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, serialize(authorizationRequest))
            .path("/")
            .httpOnly(true)
            .maxAge(Duration.ofSeconds(COOKIE_MAX_AGE_SECONDS))
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("oauth2_auth_request 쿠키 저장 완료 (state={})", authorizationRequest.getState());
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        if (authRequest != null) {
            deleteCookie(response);
            log.debug("oauth2_auth_request 쿠키 삭제 (state={})", authRequest.getState());
        }
        return authRequest;
    }

    private String serialize(OAuth2AuthorizationRequest request) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            // withoutPadding(): 쿠키 값에 '=' 패딩 문자 제거
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("OAuth2AuthorizationRequest 직렬화 실패", e);
        }
    }

    private OAuth2AuthorizationRequest deserialize(String value) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getUrlDecoder().decode(value));
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (OAuth2AuthorizationRequest) ois.readObject();
        } catch (Exception e) {
            log.warn("역직렬화 실패: {}", e.getMessage());
            return null;
        }
    }

    private Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
            .filter(c -> name.equals(c.getName()))
            .map(c -> c.getValue())
            .findFirst();
    }

    private void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
            .path("/")
            .httpOnly(true)
            .maxAge(Duration.ZERO)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
