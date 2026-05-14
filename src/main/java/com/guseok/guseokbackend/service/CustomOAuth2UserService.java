package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.common.oauth.CustomOAuth2User;
import com.guseok.guseokbackend.common.oauth.KakaoOAuth2UserInfo;
import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.Provider;
import com.guseok.guseokbackend.entity.Role;
import com.guseok.guseokbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String KAKAO_REGISTRATION_ID = "kakao";

    private final MemberRepository memberRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (!KAKAO_REGISTRATION_ID.equals(registrationId)) {
            throw new BusinessException(ErrorCode.OAUTH_UNSUPPORTED_PROVIDER);
        }

        KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(oauth2User.getAttributes());
        Member member = findOrCreateMember(userInfo);

        return new CustomOAuth2User(member, oauth2User.getAttributes());
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