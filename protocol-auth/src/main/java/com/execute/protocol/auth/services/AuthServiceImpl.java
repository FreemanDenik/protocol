package com.execute.protocol.auth.services;

import com.execute.protocol.auth.exeptions.AuthException;
import com.execute.protocol.auth.models.*;
import com.execute.protocol.core.entities.account.Account;
import com.execute.protocol.core.entities.redis.StgToken;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Primary
public class AuthServiceImpl implements AuthService{
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final StorageService storageService;
    private final JwtProvider jwtProvider;

    /**
     * Метод получения JwtResponse модель, которая содержит токены (access,refresh)
     * @param authRequest
     * @return
     * @throws AuthException
     */
    public JwtLoginResponse email(@NonNull JwtRequest authRequest) throws AuthException {
        // Получение пользователя по email
        final Account account = accountService.getAccountByEmail(authRequest.getEmail())
                // Если изменять строку "Пользователь не найден" надо изменить ее и в тестах JwtTests
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        // Сравниваем пароли полученного аккаунта и присланного
        if (passwordEncoder.matches(authRequest.getPassword(), account.getPassword())) {
            // Формируем access токен (короткоживущий)
            final String accessToken = jwtProvider.generateAccessToken(account);
            // Формируем refresh токен (долгоживущий)
            final String refreshToken = jwtProvider.generateRefreshToken(account);
            // Помещаем их в хранилище памяти (refreshStorage просто переменная, надо ее в какой-то redis запилить)
            storageService.addStorage(StgToken.builder().id(account.getEmail()).refreshToken(refreshToken).build());
            //refreshStorage.put(account.getEmail(), refreshToken);
            String[] roles = account.getRoles().stream().map(w->w.name()).toArray(String[]::new);
            // Возвращаем модель JwtLoginResponse содержащая токены
            return new JwtLoginResponse(accessToken, refreshToken, roles);
        } else {
            throw new AuthException("Неправильный логин и(или) пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            //final String saveRefreshToken = refreshStorage.get(email);
            final String saveRefreshToken = storageService.getRefreshToken(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Account account = accountService.getAccountByEmail(email)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(account);
                return new JwtResponse(accessToken, null);
            }
        }
        //return new JwtResponse(null,null);
        throw new AuthException("AccessToken не был обновлен!");
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            //final String saveRefreshToken = refreshStorage.get(email);
            final String saveRefreshToken = storageService.getRefreshToken(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Account account = accountService.getAccountByEmail(email)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(account);
                final String newRefreshToken = jwtProvider.generateRefreshToken(account);
//                refreshStorage.put(account.getEmail(), newRefreshToken);
                storageService.addStorage(StgToken.builder().id(account.getEmail()).refreshToken(newRefreshToken).build());
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
