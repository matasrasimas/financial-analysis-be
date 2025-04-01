package org.example.usecase.implementation;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;

import java.util.Arrays;
import java.util.HashSet;

public class JwtProcessorBuilder {

    private final String secretKey;

    public JwtProcessorBuilder(String secretKey) {
        this.secretKey = secretKey;
    }

    public JWTProcessor<SecurityContext> buildProcess() {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        OctetSequenceKey octetSequenceKey = new OctetSequenceKey.Builder(Base64URL.encode(secretKey))
                .algorithm(JWSAlgorithm.HS256)
                .build();

        JWKSet jwkSet = new JWKSet(octetSequenceKey);
        JWKSource<SecurityContext> updatedKeySource = new ImmutableJWKSet<>(jwkSet);

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.HS256,
                updatedKeySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder().issuer("issuer").build(),
                new HashSet<>(Arrays.asList(
                        "iss",
                        "sub",
                        "exp",
                        "userId"))));

        return jwtProcessor;
    }
}
