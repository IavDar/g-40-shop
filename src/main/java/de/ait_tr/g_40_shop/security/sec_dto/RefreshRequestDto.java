package de.ait_tr.g_40_shop.security.sec_dto;

import java.util.Objects;

public class RefreshRequestDto {
    private String refreshToken;
    // с помощью этой DTO клиент передает refreshToken на сервер с целью получения нового access Token

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshRequestDto that)) return false;
        return Objects.equals(getRefreshToken(), that.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRefreshToken());
    }

    @Override
    public String toString() {
        return String.format("Refresh request: refresh token - %s", refreshToken);
    }
}
