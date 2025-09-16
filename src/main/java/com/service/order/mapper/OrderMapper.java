package com.service.order.mapper;

import com.service.order.model.AdditionalServices;
import com.service.order.model.Address;
import com.service.order.model.CustomerDetails;
import com.service.order.model.Order;
import com.service.order.model.ServiceDetails;
import com.service.order.soap.Create;
import com.service.order.soap.Service;
import com.service.order.soap.Update;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMapper {

    public static final int VIP_CUSTOMER_ID = 123456789;
    public static final String EXTRA_DATA = "ExtraData";
    public static final String SWEDEN = "Sweden";

    public static Order toOrderModel(Create createRequest) {
        return mapCommonFields(
                createRequest.getServiceId(),
                createRequest.getServiceType(),
                createRequest.getCustomerId(),
                createRequest.getSubscriptionId(),
                createRequest.getServiceDetails(),
                createRequest.getCustomerDetails()
        );
    }

    public static Order toOrderModel(Update updateRequest) {
        return mapCommonFields(updateRequest.getServiceId(),
                updateRequest.getServiceType(),
                updateRequest.getCustomerId(),
                updateRequest.getSubscriptionId(),
                updateRequest.getServiceDetails(),
                updateRequest.getCustomerDetails());
    }

    public static Service toServiceModel(Order order) {
        Service service = new Service();
        service.setServiceId(order.getServiceId());
        service.setServiceType(order.getServiceType());
        service.setCustomerId(order.getCustomerId());
        service.setVIPCustomer(order.getVipCustomer());
        service.setSubscriptionId(order.getSubscriptionId());
        service.setServiceDetails(toServiceDetails(order.getServiceDetails()));
        service.setCustomerDetails(toCustomerDetails(order.getCustomerDetails()));

        return service;
    }

    private Order mapCommonFields(
            String serviceId,
            String serviceType,
            String customerId,
            String subscriptionId,
            com.service.order.soap.ServiceDetails serviceDetails,
            com.service.order.soap.CustomerDetails customerDetails
    ) {
        Order order = new Order();
        order.setServiceId(serviceId);
        order.setServiceType(serviceType);
        order.setCustomerId(customerId);
        if (Integer.parseInt(customerId) == VIP_CUSTOMER_ID) {
            order.setVipCustomer(true);
        }
        order.setSubscriptionId(subscriptionId);
        order.setCustomerDetails(toCustomerDetailsModel(customerDetails));

        String country = null;
        if (customerDetails.getAddress() != null && customerDetails.getAddress().getCountry() != null) {
            country = customerDetails.getAddress().getCountry();
        }
        order.setServiceDetails(toServiceDetailsModel(serviceDetails, country));

        return order;
    }

    private static com.service.order.soap.CustomerDetails toCustomerDetails(CustomerDetails customerDetails) {
        com.service.order.soap.CustomerDetails customerDetail = new com.service.order.soap.CustomerDetails();

        if (customerDetails.getAddress() != null) {
            com.service.order.soap.Address toAddress = new com.service.order.soap.Address();
            if (customerDetails.getAddress().getCity() != null) {
                toAddress.setCity(customerDetails.getAddress().getCity());
            }
            if (customerDetails.getAddress().getCountry() != null) {
                toAddress.setCountry(customerDetails.getAddress().getCountry());
            }
            if (customerDetails.getAddress().getPostalCode() != null) {
                toAddress.setPostalCode(customerDetails.getAddress().getPostalCode());
            }
            if (customerDetails.getAddress().getStreet() != null) {
                toAddress.setStreet(customerDetails.getAddress().getStreet());
            }
            customerDetail.setAddress(toAddress);
        }

        customerDetail.setName(customerDetails.getName());
        customerDetail.setContactNumber(String.valueOf(customerDetails.getContactNumber()));

        return customerDetail;
    }

    private static com.service.order.soap.ServiceDetails toServiceDetails(ServiceDetails serviceDetails) {
        com.service.order.soap.ServiceDetails serviceDetail = new com.service.order.soap.ServiceDetails();
        if (serviceDetails.getDataLimit() != null) {
            serviceDetail.setDataLimit(serviceDetails.getDataLimit());
        }
        serviceDetail.setPlanType(serviceDetails.getPlanType());
        if (serviceDetails.getRoamingEnabled() != null) {
            serviceDetail.setRoamingEnabled(serviceDetails.getRoamingEnabled());
        }
        serviceDetail.setSpecialOffer(serviceDetails.getSpecialOffer());

        if (serviceDetails.getAdditionalServices() != null) {
            com.service.order.soap.AdditionalServices additionalServices = new com.service.order.soap.AdditionalServices();
            additionalServices.getService().addAll(serviceDetails.getAdditionalServices().getService());
            serviceDetail.setAdditionalServices(additionalServices);
        }

        return serviceDetail;
    }

    private static ServiceDetails toServiceDetailsModel(com.service.order.soap.ServiceDetails serviceDetails, String country) {
        ServiceDetails serviceDetail = new ServiceDetails();
        if (serviceDetails.getAdditionalServices() != null) {
            AdditionalServices additionalServices = new AdditionalServices();
            additionalServices.setService(serviceDetails.getAdditionalServices().getService());
            serviceDetail.setAdditionalServices(additionalServices);
        }

        serviceDetail.setPlanType(serviceDetails.getPlanType());
        serviceDetail.setDataLimit(serviceDetails.getDataLimit());

        if (country != null && country.equalsIgnoreCase(SWEDEN)) {
            serviceDetail.setRoamingEnabled(serviceDetails.isRoamingEnabled());
        }

        if (serviceDetails.getDataLimit() == null && serviceDetails.getPlanType().equalsIgnoreCase("5G") ) {
            serviceDetail.setSpecialOffer(EXTRA_DATA);
        }

        return serviceDetail;
    }

    private static CustomerDetails toCustomerDetailsModel(com.service.order.soap.CustomerDetails customerDetails) {
        CustomerDetails details = new CustomerDetails();
        details.setName(customerDetails.getName());
        details.setContactNumber(String.valueOf(customerDetails.getContactNumber()));

        if (customerDetails.getAddress() != null) {
            details.setAddress(getAddress(customerDetails.getAddress()));
        }

        return details;
    }

    private static Address getAddress(com.service.order.soap.Address address) {
        Address addressModel = new Address();
        addressModel.setCity(address.getCity());
        addressModel.setCountry(address.getCountry());
        addressModel.setPostalCode(address.getPostalCode());
        addressModel.setStreet(address.getStreet());

        return addressModel;
    }
}
