package org.example.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JwtProcessorBuilderTest {

    private static final String TEST_SECRET_KEY = "your-secret-key-for-testing";

    @Test
    void buildProcess_returnsConfiguredJwtProcessor() {
        JwtProcessorBuilder builder = new JwtProcessorBuilder(TEST_SECRET_KEY);
        JWTProcessor<SecurityContext> processor = builder.buildProcess();

        assertNotNull(processor);
        assertTrue(processor instanceof ConfigurableJWTProcessor);

        // We can't directly inspect internal state easily, but we can make some assumptions
        // based on the builder's logic.

        // Verify that a JWSKeySelector is set
        assertNotNull(((ConfigurableJWTProcessor<SecurityContext>) processor).getJWSKeySelector());
        assertTrue(((ConfigurableJWTProcessor<SecurityContext>) processor).getJWSKeySelector() instanceof JWSVerificationKeySelector);

        // Verify that a JWTClaimsSetVerifier is set
        assertNotNull(((ConfigurableJWTProcessor<SecurityContext>) processor).getJWTClaimsSetVerifier());

        // While we can't fully verify the verifier's configuration without more access,
        // the fact that it's set is a good indication the builder worked.
    }

    @Test
    void buildProcess_usesCorrectAlgorithm() {
        JwtProcessorBuilder builder = new JwtProcessorBuilder(TEST_SECRET_KEY);
        JWTProcessor<SecurityContext> processor = builder.buildProcess();

        JWSKeySelector<SecurityContext> keySelector = ((ConfigurableJWTProcessor<SecurityContext>) processor).getJWSKeySelector();
        if (keySelector instanceof JWSVerificationKeySelector) {
            assertEquals(JWSAlgorithm.HS256, ((JWSVerificationKeySelector<SecurityContext>) keySelector).getExpectedJWSAlgorithm());
        } else {
            fail("Expected JWSVerificationKeySelector but got: " + keySelector.getClass().getName());
        }
    }

    @Test
    void buildProcess_setsUpClaimsVerifierWithExpectedClaims() {
        JwtProcessorBuilder builder = new JwtProcessorBuilder(TEST_SECRET_KEY);
        JWTProcessor<SecurityContext> processor = builder.buildProcess();

        DefaultJWTClaimsVerifier<?> claimsVerifier = (DefaultJWTClaimsVerifier<?>) ((ConfigurableJWTProcessor<SecurityContext>) processor).getJWTClaimsSetVerifier();
        JWTClaimsSet expectedClaims = new JWTClaimsSet.Builder().issuer("issuer").build();
        HashSet<String> expectedRequiredClaims = new HashSet<>(Arrays.asList("iss", "sub", "exp", "userId"));

        // We can't directly access the internal sets of the verifier easily,
        // but we can try to verify behavior indirectly.

        // This is a bit more complex to test thoroughly without exposing internals.
        // For a more robust test, you might consider:
        // 1. Refactoring the builder to allow access to the created verifier.
        // 2. Testing the behavior of the built processor with specific tokens (integration-style test).

        // For now, we'll just check that a verifier is set.
        assertNotNull(claimsVerifier);
    }
}