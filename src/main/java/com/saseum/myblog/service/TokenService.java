package com.saseum.myblog.service;

import com.saseum.myblog.config.jwt.TokenProvider;
import com.saseum.myblog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        System.out.println("==== refreshToken = " + refreshToken);
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("unexpected refresh token.");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
