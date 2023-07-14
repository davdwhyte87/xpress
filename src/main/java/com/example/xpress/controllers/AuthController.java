package com.example.xpress.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @GetMapping("/hello")
    public String Hello(){
        return "Hello bro";
    }
}
