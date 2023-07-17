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

    Logger logger = LoggerFactory.getLogger(AirtimeController.class);

    @PostMapping("/buy")
    public ResponseEntity<Object> BuyAirtime(@Valid @RequestBody UserBuyAirtimeReq userBuyAirtimeReq) {
        AirtimeHelper airtimeHelper = new AirtimeHelper();
        String uniqueCode = airtimeHelper.GetUniqueAirtimeCode(userBuyAirtimeReq.Biller).toString();
        logger.debug(uniqueCode);
        // prepare payment Req for xpress
        BuyAirtimeReq buyAirtimeReq = new BuyAirtimeReq();
        String generateReqId = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        buyAirtimeReq.requestId = generateReqId.substring(generateReqId.length() - 5);
        String generateUUIDNo = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        buyAirtimeReq.uniqueCode = uniqueCode;
        Details details = new Details();
        details.amount = userBuyAirtimeReq.Amount;
        details.phoneNumber = "09132058051";
        buyAirtimeReq.details = details;

        // get payment hash
        AuthUtils authUtils = new AuthUtils();
        String paymentHash = authUtils.CalculatePaymentHash(buyAirtimeReq, "hLBjoVvx3lX9upMde11ld8p7SA7fLB54_CVASPRV");
        logger.debug(paymentHash);

        // send data to express server
        try {
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseHandler.responseBuilder("ok", HttpStatus.OK, LoggedUserContext.getCurrentLoggedUser());
    }

}
