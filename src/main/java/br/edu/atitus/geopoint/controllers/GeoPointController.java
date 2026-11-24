package br.edu.atitus.geopoint.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloworld")
public class GeoPointController {

    @GetMapping
    public ResponseEntity<String> getHelloWorld(
            @RequestParam(defaultValue = "world") String name) {

        String response = "Hello, " + name + "!";
        return ResponseEntity.ok(response);
    }
}