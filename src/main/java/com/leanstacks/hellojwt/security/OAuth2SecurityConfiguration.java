package com.leanstacks.hellojwt.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2SecurityConfiguration {

    @Autowired
    private transient OAuth2Properties oauth2Properties;

    @Autowired
    private transient BasicAuthenticationProvider authenticationProvider;

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(oauth2Properties.getJwt().getSigningKey());
        tokenConverter.setVerifierKey(oauth2Properties.getJwt().getSigningKey());
        return tokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        JwtTokenStore tokenStore = new JwtTokenStore(tokenConverter());
        return tokenStore;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(authenticationProvider);

        return new ProviderManager(authenticationProviders);
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private transient OAuth2Properties oauth2Properties;

        @Autowired
        private transient AuthenticationManager authenticationManager;

        @Autowired
        private transient BasicUserDetailsService userDetailsService;

        @Autowired
        private transient JwtTokenStore tokenStore;

        @Autowired
        private transient JwtAccessTokenConverter accessTokenConverter;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            //@formatter:off
            
            clients
                .inMemory()
                    .withClient("aClient")
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("USER")
                    .scopes("read", "write")
                    .resourceIds(oauth2Properties.getResourceId())
                    .secret("aSecret")
                    .accessTokenValiditySeconds(oauth2Properties.getAccessTokenValiditySeconds())
                    .refreshTokenValiditySeconds(oauth2Properties.getRefreshTokenValiditySeconds());
            
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
        private transient OAuth2Properties oauth2Properties;

        @Autowired
        private transient JwtTokenStore tokenStore;

        @Override
        public void configure(HttpSecurity http) throws Exception {

            //@formatter:off
            
            http
            .csrf().disable()
            
            .requestMatchers().antMatchers("/api/**").and()
            
            .authorizeRequests()
            
            // Allow all OPTIONS requests
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            
            // Allow requests to public Account Security APIs
            .antMatchers(HttpMethod.GET, "/api").permitAll()
            .antMatchers(HttpMethod.POST, "/api/u/accounts").permitAll()
            
            // Allow requests to the Health Check API(s)
            .antMatchers(HttpMethod.GET, "/api/o/actuators/health").permitAll()
            
            // Require Authorization
            .anyRequest().access("#oauth2.hasScope('read')")
            
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            
            //@formatter:on

        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

            //@formatter:off
            
            resources
                .resourceId(oauth2Properties.getResourceId())
                .tokenStore(tokenStore);
            
            //@formatter:on

        }

    }

}
