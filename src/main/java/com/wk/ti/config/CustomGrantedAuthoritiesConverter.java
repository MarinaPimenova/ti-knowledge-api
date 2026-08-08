package com.wk.ti.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wk.ti.user.util.SecurityContextUtil.getAuthorities;

@SuppressWarnings("unchecked")
@Component
public class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract roles from custom claim
        Map<String, Object> attributes = jwt.getClaims();
        List<String> roles = getAuthorities((List<String>) attributes.get("cognito:groups"));

        // If roles claim is not present, return empty list of authorities
        if (roles == null) {
            return Collections.emptyList();
        }

        // Map roles to GrantedAuthorities
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}

