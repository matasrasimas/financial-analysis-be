package org.example.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.example.exception.ExpiredJwtException;
import org.example.usecase.TokenValidator;

import java.text.ParseException;
import java.util.Optional;

public class JwtValidator implements TokenValidator {
    private final JWTProcessor<SecurityContext> jwtProcessor;

    public JwtValidator(JWTProcessor<SecurityContext> jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public Optional<String> getUserIdFromJwt(String token) {
        try {
            JWTClaimsSet claimsSet = jwtProcessor.process(token, null);
            String userId = claimsSet.getClaim("userId").toString();
            return Optional.ofNullable(userId);
        } catch (ParseException | JOSEException | BadJOSEException e) {
            if(e.getMessage().equals("Expired JWT"))
                throw new ExpiredJwtException("The provided jwt is expired");
            return Optional.empty();
        }
    }
}
