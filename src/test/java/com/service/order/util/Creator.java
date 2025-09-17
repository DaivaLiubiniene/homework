package com.service.order.util;

import com.service.order.model.AdditionalServices;
import com.service.order.model.Address;
import com.service.order.model.CustomerDetails;
import com.service.order.model.Order;
import com.service.order.model.ServiceDetails;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Creator {

    public static Order createOrder() {
        Address address = new Address();
        address.setStreet("Main Street 1");
        address.setCity("Joniškis");
        address.setPostalCode("12345");
        address.setCountry("Lithuania");

        ServiceDetails serviceDetails = new ServiceDetails();
        serviceDetails.setPlanType("5G");
        serviceDetails.setRoamingEnabled(true);
        serviceDetails.setAdditionalServices(new AdditionalServices(List.of("MMS", "Voicemail")));

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setName("John Doe");
        customerDetails.setAddress(address);
        customerDetails.setContactNumber("+46701234567");

        Order order = new Order();
        order.setServiceId("007");
        order.setServiceType("MobileDataService");
        order.setCustomerId("123456789");
        order.setVipCustomer(true);
        order.setSubscriptionId("987654321");
        order.setServiceDetails(serviceDetails);
        order.setCustomerDetails(customerDetails);

        return order;
    }
}
