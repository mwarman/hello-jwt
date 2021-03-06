package com.leanstacks.hellojwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    public transient BasicUserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken token) throws AuthenticationException {

        if (token.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        if (!token.getCredentials().equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return userDetails;

    }

}
