package com.guseok.guseokbackend.controller;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.common.jwt.JwtProvider;
import com.guseok.guseokbackend.common.response.ApiResponse;
import com.guseok.guseokbackend.dto.TokenResponse;
import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.Role;
import com.guseok.guseokbackend.repository.MemberRepository;
import com.guseok.guseokbackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
        @RequestHeader("Refresh-Token") String refreshToken
    ) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(refreshToken);

        if (!refreshTokenService.isValid(memberId, refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String role = "ROLE_" + member.getRole().name();
        String newAccessToken = jwtProvider.generateAccessToken(memberId, role);
        String newRefreshToken = jwtProvider.generateRefreshToken(memberId);

        refreshTokenService.save(memberId, newRefreshToken, jwtProvider.getRefreshTokenExpiryMs());

        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(newAccessToken, newRefreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        @RequestHeader("Refresh-Token") String refreshToken
    ) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(refreshToken);
        refreshTokenService.delete(memberId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}