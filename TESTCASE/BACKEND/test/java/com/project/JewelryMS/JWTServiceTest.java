package com.project.JewelryMS;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.service.JWTservice;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    private static final String SECRET_KEY = "HT4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";
    private static final String USERNAME = "testUser";
    private static final String TOKEN = "dummyToken";

    @InjectMocks
    private JWTservice jwtService;

    @Mock
    private AuthenticationRepository authenticationRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAUsername(USERNAME);
    }

    @Test
    void generateToken_shouldReturnToken() {
        String token = jwtService.generateToken(account);
        assertNotNull(token);
    }

    @Test
    void extractAllClaims_shouldReturnClaims() {
        String token = jwtService.generateToken(account);
        Claims claims = jwtService.extractAllClaims(token);
        assertEquals(USERNAME, claims.getSubject());
    }

    @Test
    void extractAccount_shouldReturnAccount() {
        when(authenticationRepository.findAccountByUsername(USERNAME)).thenReturn(account);

        String token = jwtService.generateToken(account);
        Account extractedAccount = jwtService.extractAccount(token);

        assertNotNull(extractedAccount);
        assertEquals(USERNAME, extractedAccount.getUsername());
        verify(authenticationRepository, times(1)).findAccountByUsername(USERNAME);
    }

    @Test
    void isTokenExpired_shouldReturnFalse() {
        String token = jwtService.generateToken(account);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void extractExpiration_shouldReturnExpirationDate() {
        String token = jwtService.generateToken(account);
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
    }

    @Test
    void extractClaim_shouldReturnExpectedClaim() {
        String token = jwtService.generateToken(account);
        String username = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals(USERNAME, username);
    }
}
