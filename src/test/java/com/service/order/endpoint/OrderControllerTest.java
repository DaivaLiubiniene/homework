package com.service.order.endpoint;

import com.service.order.exception.InvalidRequestException;
import com.service.order.model.Order;
import com.service.order.service.OrderService;
import com.service.order.soap.Create;
import com.service.order.soap.Delete;
import com.service.order.soap.Read;
import com.service.order.soap.Response;
import com.service.order.soap.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.ResourceSource;

import java.io.IOException;

import static com.service.order.util.Creator.createOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderEndpoint orderEndpoint;

    private Jaxb2Marshaller marshaller;

    @BeforeEach
    void setUp() {
        marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.service.order.soap");
        try {
            marshaller.afterPropertiesSet();
        } catch (Exception e) {
            fail("Failed to initialize JAXB Marshaller: " + e.getMessage());
        }
    }

    @Test
    void createService_shouldReturnSuccess_whenOrderServiceSucceeds() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("CreateServiceRequest.xml"));
        Create request = (Create) marshaller.unmarshal(xmlSource);

        doNothing().when(orderService).createService(any(Order.class));

        Response response = orderEndpoint.createService(request);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals("Service activated successfully", response.getMessage());

        verify(orderService, Mockito.times(1)).createService(any(Order.class));
    }

    @Test
    void createService_shouldReturnError_whenInvalidContactNumber() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("CreateInvalidServiceRequest.xml"));
        Create request = (Create) marshaller.unmarshal(xmlSource);
        String errorMessage = "Invalid contact number format";
        InvalidRequestException mockException = new InvalidRequestException(errorMessage);

        doThrow(mockException).when(orderService).createService(any(Order.class));

        Response response = orderEndpoint.createService(request);

        assertNotNull(response);
        assertEquals("Error", response.getStatus());
        assertEquals(400, response.getErrorCode());
        assertEquals(errorMessage, response.getErrorMessage());

        verify(orderService, Mockito.times(1)).createService(any(Order.class));
    }

    @Test
    void getService_shouldReturnResponse_whenServiceIsFound() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("ReadRequest.xml"));
        Read request = (Read) marshaller.unmarshal(xmlSource);

        var order = createOrder();

        when(orderService.getService(any())).thenReturn(order);

        Response response = orderEndpoint.getService(request);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(order.getServiceId(), response.getService().getServiceId());

        verify(orderService, Mockito.times(1)).getService(anyString());
    }

    @Test
    void getService_shouldReturnError_whenServiceNotFound() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("ReadRequest.xml"));
        Read request = (Read) marshaller.unmarshal(xmlSource);
        String errorMessage = "Service with ID 001 not found";
        InvalidRequestException mockException = new InvalidRequestException(errorMessage);

        doThrow(mockException).when(orderService).getService(anyString());

        Response response = orderEndpoint.getService(request);

        assertNotNull(response);
        assertEquals("Error", response.getStatus());
        assertEquals(404, response.getErrorCode());
        assertEquals(errorMessage, response.getErrorMessage());

        verify(orderService, Mockito.times(1)).getService(anyString());
    }

    @Test
    void updateService_shouldReturnResponse_whenServiceUpdated() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("UpdateRequest.xml"));
        Update request = (Update) marshaller.unmarshal(xmlSource);

        var order = createOrder();

        when(orderService.updateService(any())).thenReturn(order);

        Response response = orderEndpoint.updateService(request);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(order.getServiceId(), response.getService().getServiceId());

        verify(orderService, Mockito.times(1)).updateService(any());
    }

    @Test
    void updateService_shouldReturnError_whenServiceNotFound() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("UpdateRequest.xml"));
        Update request = (Update) marshaller.unmarshal(xmlSource);
        String errorMessage = "Service with ID 001 not found";
        InvalidRequestException mockException = new InvalidRequestException(errorMessage);

        doThrow(mockException).when(orderService).updateService(any());

        Response response = orderEndpoint.updateService(request);

        assertNotNull(response);
        assertEquals("Error", response.getStatus());
        assertEquals(404, response.getErrorCode());
        assertEquals(errorMessage, response.getErrorMessage());

        verify(orderService, Mockito.times(1)).updateService(any());
    }

    @Test
    void deleteService_shouldReturnResponse_whenServiceDeleted() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("DeleteRequest.xml"));
        Delete request = (Delete) marshaller.unmarshal(xmlSource);

        doNothing().when(orderService).deleteService(anyString());

        Response response = orderEndpoint.deleteService(request);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals("Service deleted successfully", response.getMessage());

        verify(orderService, Mockito.times(1)).deleteService(anyString());
    }

    @Test
    void deleteService_shouldReturnError_whenServiceNotFound() throws IOException {
        ResourceSource xmlSource = new ResourceSource(new ClassPathResource("DeleteRequest.xml"));
        Delete request = (Delete) marshaller.unmarshal(xmlSource);
        String errorMessage = "Service with ID 001 not found";
        InvalidRequestException mockException = new InvalidRequestException(errorMessage);

        doThrow(mockException).when(orderService).deleteService(any());

        Response response = orderEndpoint.deleteService(request);

        assertNotNull(response);
        assertEquals("Error", response.getStatus());
        assertEquals(404, response.getErrorCode());
        assertEquals(errorMessage, response.getErrorMessage());

        verify(orderService, Mockito.times(1)).deleteService(any());
    }

}
