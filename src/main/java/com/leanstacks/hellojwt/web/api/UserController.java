package com.leanstacks.hellojwt.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/u/users")
public class UserController {

    @Autowired
    private transient UserDetailsService userDetailsService;

    @RequestMapping(path = "/current",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetails> getCurrentUser() {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getName());
        if (userDetails == null) {
            return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);

    }

}
