package com.example.xpress;

import com.example.xpress.controllers.AuthController;
import com.example.xpress.models.requests.LoginRequest;
import com.example.xpress.utils.DummyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static MockMvcResultMatchers matcher ;
    @Autowired
    MockMvc mvc;

    @Before("")
    public void Setup(){
        MockitoAnnotations.openMocks(this);

    }


    @Test
    public void LoginSuccess() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.Email = "jenny@gmail.com";
        loginRequest.Password ="YEUH839jDHJUND";
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult result = mvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        DummyData.LoginToekn = JsonPath.read(result.getResponse().getContentAsString(), "$.data.token");
//        System.out.println(token);
    }


}
