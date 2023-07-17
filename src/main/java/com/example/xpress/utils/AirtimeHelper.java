package com.example.xpress.utils;

import com.example.xpress.controllers.AirtimeController;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AirtimeHelper {
    Logger logger = LoggerFactory.getLogger(AirtimeController.class);
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

