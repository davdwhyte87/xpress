package com.example.xpress.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


// this checks every request to make sure that the user is authenticated.
public class UserAuthInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(UserAuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.info("Hit route ......");
        // check if the user has the right authorization token
        AuthUtils authUtils = new AuthUtils();
        String email;
        try{
            email = authUtils.ParseJwt(request.getHeader("Authorization")).getBody().get("email").toString();
        }catch ( Exception exception){
           logger.debug(exception.getMessage());
           ResponseEntity<Object> responseHandler = ResponseHandler.responseBuilder("error", HttpStatus.UNAUTHORIZED, "Token rejected");
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(responseHandler));
            return false;
        }

        // store user email in thread
        LoggedUserContext.setCurrentLoggedUser(email);


        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        LoggedUserContext.clear();
    }

}

