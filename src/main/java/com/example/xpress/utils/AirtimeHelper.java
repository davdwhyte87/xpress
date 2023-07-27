package com.example.xpress.utils;

import com.example.xpress.controllers.AirtimeController;
import com.example.xpress.models.requests.BuyAirtimeReq;
import com.example.xpress.models.requests.Details;
import com.example.xpress.models.requests.UserBuyAirtimeReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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


@Component
public class AirtimeHelper {
    Logger logger = LoggerFactory.getLogger(AirtimeController.class);

    public boolean submitBuyRequestAtXpress(UserBuyAirtimeReq userBuyAirtimeReq, String uniqueCode){
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
            if (response.statusCode() != 200){
              return false;
            }
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
    public AtomicReference<String> GetUniqueAirtimeCode(String billerName) {
        String content = "{\n" +
                "\n" +
                "\"size\": 5,  \n" +
                "\n" +
                "\"page\": 0  \n" +
                "\n" +
                "}";

        AtomicReference<String> resCode = new AtomicReference<>("");
        // send data to express server
        try{
            var uri = URI.create("https://billerstest.xpresspayments.com:9603/api/v1/products?categoryId=1");
            var client = HttpClient.newHttpClient();
            Map<String, String> uniqueCodeList = new HashMap<>();
            var request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofMinutes(3))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer jD3FFF9MiN7MoyyWsIf6WXQsn44nPvji_CVASPUB")
                    .header("Channel", "API")
                    .POST(HttpRequest.BodyPublishers.ofString(content))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // get data if response is ok
            if (response.statusCode() == 200){

                JSONArray productList = JsonPath.read(response.body(), "$.data.productDTOList");
                int productListCount = productList.size();

                // loop through products list and get each product data, store in hashmap
                for (int i=0; i<productListCount; i++){

                    String biller = JsonPath.read(response.body(), "$.data.productDTOList["+i+"].billerName");
                    String code = JsonPath.read(response.body(), "$.data.productDTOList["+i+"].uniqueCode");
                    uniqueCodeList.put(biller,code);
                }

                logger.debug(uniqueCodeList.toString());
                uniqueCodeList.forEach((key, value)->{
                    if (Objects.equals(billerName, key)){
                        resCode.set(value);
                    }
                });
            }

            logger.debug(resCode.toString());

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        return resCode;

    }
}

