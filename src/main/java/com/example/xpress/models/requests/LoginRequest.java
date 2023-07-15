package com.example.xpress.models.requests;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class LoginRequest {
    @jakarta.persistence.Id
    public String Id;
    @NotBlank
    public String Email;
    @NotBlank
    public String Password;
}
