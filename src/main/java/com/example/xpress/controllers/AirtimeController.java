package com.example.xpress.controllers;


import com.example.xpress.models.requests.BuyAirtimeReq;
import com.example.xpress.models.requests.Details;
import com.example.xpress.models.requests.UserBuyAirtimeReq;
import com.example.xpress.utils.AirtimeHelper;
import com.example.xpress.utils.AuthUtils;
import com.example.xpress.utils.LoggedUserContext;
import com.example.xpress.utils.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.validation.Valid;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/airtime")
public class AirtimeController {

    @Autowired
    ApplicationContext context;
    Logger logger = LoggerFactory.getLogger(AirtimeController.class);

    @PostMapping("/buy")
    public ResponseEntity<Object> BuyAirtime(@Valid @RequestBody UserBuyAirtimeReq userBuyAirtimeReq) {
        // get airtime unique code from xpress API
        AirtimeHelper airtimeHelper = context.getBean(AirtimeHelper.class);
        String uniqueCode = airtimeHelper.GetUniqueAirtimeCode(userBuyAirtimeReq.Biller).toString();
        logger.debug(uniqueCode);
        // submit purchase request to xpress pay
        airtimeHelper.submitBuyRequestAtXpress(userBuyAirtimeReq, uniqueCode);
        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, LoggedUserContext.getCurrentLoggedUser());
    }
}
