package com.guseok.guseokbackend.common.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        Object id = attributes.get("id");
        return id == null ? null : String.valueOf(id);
    }

    public String getNickname() {
        Map<String, Object> properties = getProperties();
        Object nickname = properties.get("nickname");
        if (nickname != null) {
            return String.valueOf(nickname);
        }

        Object profileNickname = getProfile().get("nickname");
        return profileNickname == null ? null : String.valueOf(profileNickname);
    }

    public String getProfileImageUrl() {
        Map<String, Object> properties = getProperties();
        Object profileImage = properties.get("profile_image");
        if (profileImage != null) {
            return String.valueOf(profileImage);
        }

        Object profileImageUrl = getProfile().get("profile_image_url");
        return profileImageUrl == null ? null : String.valueOf(profileImageUrl);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getProperties() {
        Object properties = attributes.get("properties");
        return properties instanceof Map<?, ?> ? (Map<String, Object>) properties : Map.of();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getKakaoAccount() {
        Object kakaoAccount = attributes.get("kakao_account");
        return kakaoAccount instanceof Map<?, ?> ? (Map<String, Object>) kakaoAccount : Map.of();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getProfile() {
        Object profile = getKakaoAccount().get("profile");
        return profile instanceof Map<?, ?> ? (Map<String, Object>) profile : Map.of();
    }
}