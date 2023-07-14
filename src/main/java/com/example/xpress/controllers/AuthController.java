package com.example.xpress.controllers;





import com.example.xpress.models.requests.LoginRequest;
import com.example.xpress.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthController {
    @PostMapping("/hello")
    public ResponseEntity<Object> Hello(@Valid @RequestBody LoginRequest loginRequest){

        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, loginRequest);
    }
}
