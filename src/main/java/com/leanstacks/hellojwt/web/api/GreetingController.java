package com.leanstacks.hellojwt.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leanstacks.hellojwt.model.Greeting;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Greeting>> getGreetings() {

        List<Greeting> greetings = new ArrayList<Greeting>();

        greetings.add(new Greeting("Hello World"));
        greetings.add(new Greeting("Hola Mundo"));

        return new ResponseEntity<List<Greeting>>(greetings, HttpStatus.OK);

    }

}
