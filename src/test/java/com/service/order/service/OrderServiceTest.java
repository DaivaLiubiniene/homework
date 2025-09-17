package com.service.order.service;

import com.service.order.exception.InvalidRequestException;
import com.service.order.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static com.service.order.util.Creator.createOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService service;

    @Mock
    private Map<String, Order> serviceDatabase;

    @BeforeEach
    void setUp() {
        serviceDatabase = new HashMap<>();
        ReflectionTestUtils.setField(service, "serviceDatabase", serviceDatabase);
    }

    @Test
    void createService_shouldAddOrder_whenIdDoesNotExist() {
        var order = createOrder();

        service.createService(order);

        assertTrue(serviceDatabase.containsKey("007"));
        assertEquals(order, serviceDatabase.get("007"));

    }

    @Test
    void createService_ServiceAlreadyExists_ThrowsInvalidRequestException() {
        var order = createOrder();
        serviceDatabase.put("007", order);

        Order newOrder = new Order();
        newOrder.setServiceId("007");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.createService(newOrder));
        assertEquals("Service with ID 007 already exists", exception.getMessage());
    }

    @Test
    void deleteService_shouldDeleteService_whenItExists() {
        var order = createOrder();
        serviceDatabase.put("007", order);

        service.deleteService(order.getServiceId());

        assertFalse(serviceDatabase.containsKey("007"));
    }

    @Test
    void deleteService_ServiceDoesNotExist_ThrowsInvalidRequestException() {
        var order = createOrder();
        serviceDatabase.put("007", order);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.deleteService("006"));
        assertEquals("Service with ID 006 not found", exception.getMessage());
    }

    @Test
    void getService_shouldGetService_whenItExists() {
        var order = createOrder();
        serviceDatabase.put("007", order);

        var result = service.getService(order.getServiceId());

        assertTrue(serviceDatabase.containsKey("007"));
        assertEquals(order, result);
    }

    @Test
    void getService_ServiceDoesNotExist_ThrowsInvalidRequestException() {
        var order = createOrder();
        serviceDatabase.put("007", order);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.getService("006"));
        assertEquals("Service with ID 006 not found", exception.getMessage());
    }

    @Test
    void updateService_shouldUpdateService_whenItExists() {
        var order = createOrder();
        serviceDatabase.put("007", order);
        order.setServiceType("somethingDifferent");

        var result = service.updateService(order);

        assertTrue(serviceDatabase.containsKey("007"));
        assertEquals("somethingDifferent", result.getServiceType());
    }

    @Test
    void updateService_whenContactNumberInvalid_ThrowsInvalidRequestException() {
        var order = createOrder();
        serviceDatabase.put("007", order);
        var customerDetails = order.getCustomerDetails();
        customerDetails.setContactNumber("123");
        order.setCustomerDetails(customerDetails);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.updateService(order));
        assertEquals("Invalid contact number format", exception.getMessage());
    }

    @Test
    void updateService_ServiceDoesNotExist_ThrowsInvalidRequestException() {
        var order = createOrder();
        serviceDatabase.put("006", order);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.updateService(order));
        assertEquals("Service with ID 007 not found", exception.getMessage());
    }
}
