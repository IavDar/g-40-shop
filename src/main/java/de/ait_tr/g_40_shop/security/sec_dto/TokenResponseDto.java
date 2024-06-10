package de.ait_tr.g_40_shop.security.sec_dto;

import java.util.Objects;

public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    // Всегда генерируем equals и hashCode, чтобы лучше работали коллекции, не считали продукты дубликатами:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenResponseDto that)) return false;
        return Objects.equals(getAccessToken(), that.getAccessToken()) && Objects.equals(getRefreshToken(), that.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccessToken(), getRefreshToken());
    }

    @Override
    public String toString() {
        return String.format("Token response: access token - %s, refresh token - %s",
                accessToken, refreshToken);
    }
}
