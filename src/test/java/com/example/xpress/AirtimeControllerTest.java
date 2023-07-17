package com.example.xpress;

import com.example.xpress.controllers.AirtimeController;
import com.example.xpress.controllers.AuthController;
import com.example.xpress.models.requests.LoginRequest;
import com.example.xpress.models.requests.UserBuyAirtimeReq;
import com.example.xpress.utils.DummyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(AirtimeController.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AirtimeControllerTest {

    private static MockMvcResultMatchers matcher ;
    @Autowired
    MockMvc mvc;

    @Before("")
    public void Setup(){
        MockitoAnnotations.openMocks(this);

    }



    // this test attempts to purchase aitime for an unauthoruized user
    @Test
    public void BuyAirtimeFailUnAuthorized() throws Exception{

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/airtime/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "UNJNDKJNF039849KJNKNC8948J CDN DKnn3498jnCKKJN")
                .content("");

        mvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void BuyAirtimePass() throws Exception{

        // prepare request to be sent to buy airtime api
        ObjectMapper objectMapper = new ObjectMapper();
        UserBuyAirtimeReq userBuyAirtimeReq = new UserBuyAirtimeReq();
        userBuyAirtimeReq.Amount = 200;
        String content =objectMapper.writeValueAsString(userBuyAirtimeReq);

        // prepare mock request
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/airtime/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", DummyData.LoginToekn == null ?"":DummyData.LoginToekn)
                .content(content);

        mvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}

