package com.service.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetails {

    private String planType;
    private String dataLimit;
    private Boolean roamingEnabled;
    private AdditionalServices additionalServices;
    private String specialOffer;
}
