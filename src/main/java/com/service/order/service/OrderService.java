package com.service.order.service;

import com.service.order.exception.InvalidRequestException;
import com.service.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    public static final String SERVICE_WITH_ID = "Service with ID ";
    public static final String NOT_FOUND = " not found";
    public static final String CONTACT_NUMBER_REGEX = "^\\+\\d{11,15}$";
    public static final String ALREADY_EXISTS = " already exists";
    private final Map<String, Order> serviceDatabase = new HashMap<>();

    public void createService(Order order) {

        if (!order.getCustomerDetails().getContactNumber().matches(CONTACT_NUMBER_REGEX)) {
            throw new InvalidRequestException("Invalid contact number format");
        }

        if (!serviceDatabase.containsKey(order.getServiceId())){
            serviceDatabase.put(order.getServiceId(), order);
        } else {
            throw new InvalidRequestException(SERVICE_WITH_ID + order.getServiceId() + ALREADY_EXISTS);
        }
    }

    public Order getService(String serviceId) {
        if (serviceDatabase.containsKey(serviceId)){
            return serviceDatabase.get(serviceId);
        } else {
            throw new InvalidRequestException(SERVICE_WITH_ID + serviceId + NOT_FOUND);
        }
    }

    public void deleteService(String serviceId) {
        if (serviceDatabase.containsKey(serviceId)){
            serviceDatabase.remove(serviceId);
        } else {
            throw new InvalidRequestException(SERVICE_WITH_ID + serviceId + NOT_FOUND);
        }
    }

    public Order updateService(Order order) {
        if (serviceDatabase.containsKey(order.getServiceId())){
            return serviceDatabase.put(order.getServiceId(), order);
        } else {
            throw new InvalidRequestException(SERVICE_WITH_ID + order.getServiceId() + NOT_FOUND);
        }
    }

}
