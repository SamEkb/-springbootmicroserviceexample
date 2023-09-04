package com.kilanov.jwtauthoritiesparser;

import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtClaimsParser {
    private Jwt<?, ?> jwtObject;

    public JwtClaimsParser(String jwt, String secretToken) {
        jwtObject = parseJwt(jwt, secretToken);
    }

    private Jwt<?, ?> parseJwt(String jwt, String secretToken) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        return parser.parse(jwt);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<Map<String, String>> scopes = ((Claims) jwtObject.getBody()).get("scope", List.class);

        return scopes.stream()
                .map(scopeMap -> new SimpleGrantedAuthority(scopeMap.get("authority")))
                .collect(Collectors.toList());
    }

    public String getJwtSubject() {
        return ((Claims) jwtObject.getBody()).getSubject();
    }
}
