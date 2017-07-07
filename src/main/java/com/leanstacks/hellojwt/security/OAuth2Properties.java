package com.leanstacks.hellojwt.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hellojwt.oauth2")
public class OAuth2Properties {

    private int accessTokenValiditySeconds = 900;

    private int refreshTokenValiditySeconds = 3600;

    private String resourceId;

    private final Jwt jwt = new Jwt();

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public static class Jwt {

        private String signingKey;

        public String getSigningKey() {
            return signingKey;
        }

        public void setSigningKey(final String signingKey) {
            this.signingKey = signingKey;
        }

    }

}
