package org.example.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.example.exception.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtValidatorTest {

    @Mock
    private JWTProcessor<SecurityContext> jwtProcessor;

    @InjectMocks
    private JwtValidator jwtValidator;

    @Test
    void getUserIdFromJwt_validToken_returnsUserId() throws ParseException, JOSEException, BadJOSEException {
        String token = "valid.jwt.token";
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("userId", "user123")
                .build();
        when(jwtProcessor.process(token, null)).thenReturn(claimsSet);

        Optional<String> userIdOptional = jwtValidator.getUserIdFromJwt(token);

        assertTrue(userIdOptional.isPresent());
        assertEquals("user123", userIdOptional.get());
    }

    @Test
    void getUserIdFromJwt_parseException_returnsEmptyOptional() throws ParseException, JOSEException, BadJOSEException {
        String invalidToken = "invalid-jwt";
        when(jwtProcessor.process(invalidToken, null)).thenThrow(new ParseException("Failed to parse", 0));

        Optional<String> userIdOptional = jwtValidator.getUserIdFromJwt(invalidToken);

        assertTrue(userIdOptional.isEmpty());
    }

    @Test
    void getUserIdFromJwt_joseException_returnsEmptyOptional() throws ParseException, JOSEException, BadJOSEException {
        String invalidToken = "invalid.jwt";
        when(jwtProcessor.process(invalidToken, null)).thenThrow(new JOSEException("JOSE error"));

        Optional<String> userIdOptional = jwtValidator.getUserIdFromJwt(invalidToken);

        assertTrue(userIdOptional.isEmpty());
    }

    @Test
    void getUserIdFromJwt_badJOSEException_returnsEmptyOptional() throws ParseException, JOSEException, BadJOSEException {
        String invalidToken = "bad.jwt";
        when(jwtProcessor.process(invalidToken, null)).thenThrow(new BadJOSEException("Bad JOSE"));

        Optional<String> userIdOptional = jwtValidator.getUserIdFromJwt(invalidToken);

        assertTrue(userIdOptional.isEmpty());
    }

    @Test
    void getUserIdFromJwt_expiredJwtException_throwsExpiredJwtException() throws ParseException, JOSEException, BadJOSEException {
        String expiredToken = "expired.jwt";
        BadJOSEException expiredException = new BadJOSEException("Expired JWT");
        when(jwtProcessor.process(expiredToken, null)).thenThrow(expiredException);

        assertThrows(ExpiredJwtException.class, () -> jwtValidator.getUserIdFromJwt(expiredToken));
    }
}