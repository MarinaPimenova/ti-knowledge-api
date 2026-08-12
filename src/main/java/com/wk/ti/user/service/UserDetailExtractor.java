package com.wk.ti.user.service;

import com.wk.ti.user.model.UserDetail;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public class UserDetailExtractor {
    private static final String USER_ROLE = "user";
    private static final String MODERATOR_ROLE = "moderator";
    private static final String ADMIN_ROLE = "admin";
    private static final String USER_ID = "sub";
    private static final String DEFAULT_EMAIL = "";
    private static final String DEFAULT_GIVEN_NAME = "google";
    private static final String DEFAULT_FAMILY_NAME = "";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String GIVEN_NAME_ATTRIBUTE = "given_name";
    private static final String FAMILY_NAME_ATTRIBUTE = "family_name";
    private static final String GROUPS_ATTRIBUTE = "groups";

    public UserDetail extractor(Jwt jwt) {
        Assert.notNull(jwt, "jwt can't be null");
        Map<String, Object> attributes = jwt.getClaims();

        List<String> authorities = extractRoles(attributes);
        return UserDetail.builder()
                .username(getUserId(attributes))
                .email(getEmail(attributes))
                .givenName(getGivenName(attributes))
                .familyName(getFamilyName(attributes))
                .roles(authorities.isEmpty() ? List.of(USER_ROLE) : authorities)
                .build();
    }

    private List<String> extractRoles(Map<String, Object> attributes) {
        List<String> authorities = (List<String>) attributes.get(GROUPS_ATTRIBUTE);

        return authorities == null ? List.of() : authorities;
    }

    private String getEmail(Map<String, Object> attributes) {
        return attributes.get(EMAIL_ATTRIBUTE) == null ? DEFAULT_EMAIL : (String) attributes.get(EMAIL_ATTRIBUTE);
    }

    private String getGivenName(Map<String, Object> attributes) {
        return attributes.get(GIVEN_NAME_ATTRIBUTE) == null ? DEFAULT_GIVEN_NAME : (String) attributes.get(GIVEN_NAME_ATTRIBUTE);
    }

    private String getFamilyName(Map<String, Object> attributes) {
        return attributes.get(FAMILY_NAME_ATTRIBUTE) == null ? DEFAULT_FAMILY_NAME : (String) attributes.get(FAMILY_NAME_ATTRIBUTE);
    }

    private String getUserId(Map<String, Object> attributes) {
        return attributes.get(USER_ID) == null ? "" : (String) attributes.get(USER_ID);
    }

}
