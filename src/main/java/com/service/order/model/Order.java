package com.service.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String serviceId;
    private String serviceType;
    private String customerId;
    private Boolean vipCustomer;
    private String subscriptionId;
    private ServiceDetails serviceDetails;
    private CustomerDetails customerDetails;
}
