package com.example.xpress.controllers;


import com.example.xpress.models.requests.BuyAirtimeReq;
import com.example.xpress.models.requests.Details;
import com.example.xpress.models.requests.UserBuyAirtimeReq;
import com.example.xpress.utils.AuthUtils;
import com.example.xpress.utils.LoggedUserContext;
import com.example.xpress.utils.ResponseHandler;
import com.example.xpress.utils.UserAuthInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/airtime")
public class AirtimeController {

    Logger logger = LoggerFactory.getLogger(AirtimeController.class);
    @PostMapping("/buy")
    public ResponseEntity<Object> BuyAirtime(@Valid @RequestBody UserBuyAirtimeReq userBuyAirtimeReq){

        // prepare payment Req for xpress
        BuyAirtimeReq buyAirtimeReq = new BuyAirtimeReq();
        String generateReqId = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        buyAirtimeReq.requestId = generateReqId.substring( generateReqId.length() - 5);
        String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        buyAirtimeReq.uniqueCode = "MTN_"+generateUUIDNo.substring( generateUUIDNo.length() - 5);
        Details details = new Details();
        details.amount =userBuyAirtimeReq.Amount;
        details.phoneNumber = "09132058051";
        buyAirtimeReq.details = details;

        // get payment hash
        AuthUtils authUtils = new AuthUtils();
        String paymentHash = authUtils.CalculatePaymentHash(buyAirtimeReq, "hLBjoVvx3lX9upMde11ld8p7SA7fLB54_CVASPRV");
        logger.debug( paymentHash);

        // send data to express server
        try{
            var uri = URI.create("https://billerstest.xpresspayments.com:9603/api/v1/airtime/fulfil");
            var client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(buyAirtimeReq);
            var request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofMinutes(3))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer jD3FFF9MiN7MoyyWsIf6WXQsn44nPvji_CVASPUB")
                    .header("Authentication", "Bearer jD3FFF9MiN7MoyyWsIf6WXQsn44nPvji_CVASPUB")
                    .header("PaymentHash", paymentHash)
                    .header("Channel", "API")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug(response.body());
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, LoggedUserContext.getCurrentLoggedUser());
    }
}
