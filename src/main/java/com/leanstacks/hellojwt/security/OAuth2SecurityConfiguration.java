package com.leanstacks.hellojwt.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2SecurityConfiguration {

    private static final String SIGNING_KEY = "SigningKeyValue";
    private static final String RESOURCE_ID = "HelloJWT";

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(SIGNING_KEY);
        tokenConverter.setVerifierKey(SIGNING_KEY);
        return tokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        JwtTokenStore tokenStore = new JwtTokenStore(tokenConverter());
        return tokenStore;
    }

    @Bean
    public BasicAuthenticationProvider basicAuthenticationProvider() {
        return new BasicAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(basicAuthenticationProvider());

        return new ProviderManager(authenticationProviders);
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        public AuthenticationManager authenticationManager;

        @Autowired
        public BasicUserDetailsService userDetailsService;

        @Autowired
        public JwtTokenStore tokenStore;

        @Autowired
        public JwtAccessTokenConverter accessTokenConverter;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            //@formatter:off
            
            clients
                .inMemory()
                    .withClient("aClient")
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("USER")
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID)
                    .secret("aSecret");
            
            //@formatter:on

        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            //@formatter:off
            
            endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter);
            
            //@formatter:on

        }

    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        public JwtTokenStore tokenStore;

        @Override
        public void configure(HttpSecurity http) throws Exception {

            //@formatter:off
            
            http
            .csrf().disable()
            
            .authorizeRequests()
            
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().access("#oauth2.hasScope('read')");
            
            //@formatter:on

        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

            //@formatter:off
            
            resources
                .resourceId(RESOURCE_ID)
                .tokenStore(tokenStore);
            
            //@formatter:on

        }

    }

}
