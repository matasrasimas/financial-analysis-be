package org.example.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.example.usecase.TokenGenerator;

import java.time.Instant;
import java.util.Date;

public class JwtGenerator implements TokenGenerator {

    private final String signingKey;

    public JwtGenerator(String signingKey) {
        this.signingKey = signingKey;
    }

    @Override
    public String generate(String userId) {
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("subject")
                    .issuer("issuer")
                    .expirationTime(Date.from(Instant.now().plusSeconds(86400)))
                    .claim("userId", userId)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            JWSSigner signer = new MACSigner(signingKey);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}