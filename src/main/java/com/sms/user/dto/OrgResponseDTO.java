package com.sms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgResponseDTO {

    private String id;
    private String orgname;
    private String description;
    private String orgEmail;
    private String legalName;
    private String phone;
    private String website;
    private String country;
    private String state;
    private String city;
    private String address1;
    private String address2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
