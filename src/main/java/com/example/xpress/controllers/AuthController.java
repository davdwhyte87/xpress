package com.example.xpress.controllers;





import com.example.xpress.models.User;
import com.example.xpress.models.requests.LoginRequest;
import com.example.xpress.models.response.ErrorResp;
import com.example.xpress.models.response.LoginResponse;
import com.example.xpress.utils.AuthUtils;
import com.example.xpress.utils.DummyData;
import com.example.xpress.utils.ResponseHandler;
import com.example.xpress.utils.UserAuthInterceptor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("auth/")
public class AuthController {
//    public User GetUser(){
//        User user = new User("jndndj","Jenny Ide","jenny@gmail.com","YEUH839jDHJUND");
//
//        return user;
//    }

    Logger logger = LoggerFactory.getLogger(UserAuthInterceptor.class);
    @PostMapping("/hello")
    public ResponseEntity<Object> LoginUser(@Valid @RequestBody LoginRequest loginRequest){
        // simulate pulling user from database
        User userData = DummyData.GetUser();

        // check if auth data is valid
        if (!Objects.equals(loginRequest.Password, userData.Password)){
            return ResponseHandler.responseBuilder("error", HttpStatus.UNAUTHORIZED, "Invalid data");
        }

        logger.info("Logged user in");

        // generate jwt token
        AuthUtils authUtils = new AuthUtils();
        String token = authUtils.GenerateUserToken(userData);


        // send response
        LoginResponse loginResponse = new LoginResponse(token, "Login Ok");
        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, loginResponse);
    }
}
