package com.example.xpress.utils;

import com.example.xpress.controllers.AirtimeController;
import com.example.xpress.models.User;
import com.example.xpress.models.requests.BuyAirtimeReq;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
public class AuthUtils {
    Logger logger = LoggerFactory.getLogger(AuthUtils.class);
    public  Jws<Claims> ParseJwt(String jwtString) {
        String secret = DummyData.Secret;
        Key key = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }
    public String GenerateUserToken(User user){
        String secret = DummyData.Secret;

        Key key = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("name", user.Name)
                .claim("email", user.Email)
                .setSubject("Login")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(48, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();

        return jwtToken;
    }


    public String CalculatePaymentHash(BuyAirtimeReq object, String key){

        String HMAC_SHA512 = "HmacSHA512";

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);

        Mac mac = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(object);
            logger.debug(data);

            mac = Mac.getInstance(HMAC_SHA512);

            mac.init(secretKeySpec);

            return Hex.encodeHexString(mac.doFinal(data.getBytes()));

        } catch (NoSuchAlgorithmException | InvalidKeyException |JsonProcessingException e) {

            e.printStackTrace();

            throw new RuntimeException(e.getMessage());

        }


    }
}
