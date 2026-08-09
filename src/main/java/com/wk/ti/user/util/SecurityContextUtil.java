package com.wk.ti.user.util;

import com.wk.ti.user.model.UserDetail;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused"})
public class SecurityContextUtil {
    private static final String COGNITO_USERNAME_PREFIX = "Merck-PingFed-SAML_";
    private static final String COGNITO_GROUP_SUFFIX = "_Merck-RWDEX";

    private SecurityContextUtil() {
    }

    public static UserDetail getUserDetail(Jwt jwt) {
        Assert.notNull(jwt, "jwt can't be null");
        Map<String, Object> attributes = jwt.getClaims();

        return getUserDetail(attributes);
    }

    public static UserDetail getUserDetail(Map<String, Object> attributes) {
        Assert.notNull(attributes, "attributes can't be null");
        // cognito:username -> Merck-PingFed-SAML_ISID
        String username = ((String) attributes.get("cognito:username")).replace(COGNITO_USERNAME_PREFIX, "");
        return UserDetail.builder()
                .username(username)
                .email((String) attributes.get("email"))
                .givenName((String) attributes.get("given_name"))
                .familyName((String) attributes.get("family_name"))
                .roles(getAuthorities((List<String>) attributes.get("cognito:groups")))
                .build();
    }

    public static List<String> getAuthorities(List<String> grantedAuthorities) {
        return grantedAuthorities.stream()
                .filter(authority -> authority.contains(COGNITO_GROUP_SUFFIX))
                .map(authority -> authority.replace(COGNITO_GROUP_SUFFIX, ""))
                .collect(Collectors.toList());
    }
}
