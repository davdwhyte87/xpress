package com.example.xpress.controllers;


import com.example.xpress.utils.LoggedUserContext;
import com.example.xpress.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airtime")
public class AirtimeController {


    @PostMapping("/buy")
    public ResponseEntity<Object> BuyAirtime(){

        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, LoggedUserContext.getCurrentLoggedUser());
    }
}
