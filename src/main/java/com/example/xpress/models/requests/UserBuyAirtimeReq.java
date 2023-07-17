package com.example.xpress.models.requests;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class UserBuyAirtimeReq {

    @NotNull
    public Integer Amount;
    @NotBlank
    public String Biller;
}
