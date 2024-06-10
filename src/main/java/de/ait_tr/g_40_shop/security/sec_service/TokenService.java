package de.ait_tr.g_40_shop.security.sec_service;

import de.ait_tr.g_40_shop.domain.entity.Role;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.RoleRepository;
import de.ait_tr.g_40_shop.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenService {
    private SecretKey accessKey;
    private SecretKey refreshKey;
    private RoleRepository roleRepository;

    public TokenService(
            @Value("${key.access}") String accessSecretPhrase,
            @Value("${key.refresh}") String refreshSecretPhrase,
            RoleRepository roleRepository
    ) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretPhrase));
        this.roleRepository = roleRepository;
    }

    public String generateAccessToken(User user) { // метод генерирует AccessToken для пользователя
        // текущее время:
        LocalDateTime currentDate = LocalDateTime.now();
        // нужно определить время через 7 дней, когда истекает срок действия Access Token.
        // токен понимает объект класса Date, поэтому конвертируем LocalDateTime
        // в Instant, а потом объект класса Instant в объект Date:
        Instant expiration = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        // возвращаем token:
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(accessKey)
                .claim("roles", user.getAuthorities())
                .claim("name", user.getUsername())
                .compact(); // метод собирает токен, конвертирует его в стринг, и отдает пользователю
    }
    public String generateRefreshToken(User user) { // метод генерирует Refresh Token для пользователя
        // текущее время:
        LocalDateTime currentDate = LocalDateTime.now();
        // нужно определить время через 30 дней, когда истекает срок действия Refresh Token.
        // токен понимает объект класса Date, поэтому конвертируем LocalDateTime
        // в Instant, а потом объект класса Instant в объект Date:
        Instant expiration = currentDate.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        // возвращаем token:
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact(); // метод собирает токен, конвертирует его в стринг, и отдает пользователю
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, SecretKey key) { // метод валидации токена возвращает true,
        // если токен валиден и false, если токен не валиден. Токен проверяем при помощи секретного ключа. Этот метод делаем приватным,
        // т.к. секретные ключи никому не известны, кроме  этого TokenService.
        try {
            Jwts.parser()
                    .verifyWith(key) // в этот метод пердаем секретный ключ для валидации токена
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        String username = claims.getSubject();
        List<LinkedHashMap<String, String>> rolesList =
                (List<LinkedHashMap<String, String>>) claims.get("roles");
        Set<Role> roles = new HashSet<>();

        for (LinkedHashMap<String, String> roleEntry : rolesList) {
            String roleTitle = roleEntry.get("authority");
            Role role = roleRepository.findByTitle(roleTitle).orElse(null);
            if (role != null) {
                roles.add(role);
            }
        }

        return new AuthInfo(username, roles);
    }
}
