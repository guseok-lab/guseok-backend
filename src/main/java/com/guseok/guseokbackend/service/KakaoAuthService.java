package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.common.jwt.JwtProvider;
import com.guseok.guseokbackend.common.oauth.KakaoOAuth2UserInfo;
import com.guseok.guseokbackend.dto.TokenResponse;
import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.Provider;
import com.guseok.guseokbackend.entity.Role;
import com.guseok.guseokbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestClient.Builder restClientBuilder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse login(String kakaoAccessToken) {
        KakaoOAuth2UserInfo userInfo = fetchKakaoUserInfo(kakaoAccessToken);
        Member member = findOrCreateMember(userInfo);

        String role = "ROLE_" + member.getRole().name();
        String accessToken = jwtProvider.generateAccessToken(member.getId(), role);
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        refreshTokenService.save(member.getId(), refreshToken, jwtProvider.getRefreshTokenExpiryMs());

        return new TokenResponse(accessToken, refreshToken);
    }

    @SuppressWarnings("unchecked")
    private KakaoOAuth2UserInfo fetchKakaoUserInfo(String kakaoAccessToken) {
        try {
            Map<String, Object> attributes = restClientBuilder.build()
                .get()
                .uri(KAKAO_USER_INFO_URL)
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .body(Map.class);
            return new KakaoOAuth2UserInfo(attributes);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new BusinessException(ErrorCode.KAKAO_TOKEN_INVALID);
        }
    }

    private Member findOrCreateMember(KakaoOAuth2UserInfo userInfo) {
        String providerId = userInfo.getProviderId();
        if (providerId == null || providerId.isBlank()) {
            throw new BusinessException(ErrorCode.OAUTH_PROVIDER_ID_MISSING);
        }

        return memberRepository.findByProviderAndProviderId(Provider.KAKAO, providerId)
            .orElseGet(() -> createMember(userInfo, providerId));
    }

    private Member createMember(KakaoOAuth2UserInfo userInfo, String providerId) {
        String nickname = userInfo.getNickname();
        return memberRepository.save(Member.builder()
            .nickname(nickname == null || nickname.isBlank() ? "카카오 사용자" : nickname)
            .profileImageUrl(userInfo.getProfileImageUrl())
            .provider(Provider.KAKAO)
            .providerId(providerId)
            .role(Role.USER)
            .build());
    }
}
