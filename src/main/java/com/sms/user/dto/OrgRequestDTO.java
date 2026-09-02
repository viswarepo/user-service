package com.sms.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrgRequestDTO {

    @NotBlank(message = "Organization name is required")
    private String orgname;

    @NotBlank(message = "Organization email is required")
    private String email;
    private String description;
    private String legalName;
    private String phone;
    private String website;
    private String country;
    private String state;
    private String city;
    private String address1;
    private String address2;

}
