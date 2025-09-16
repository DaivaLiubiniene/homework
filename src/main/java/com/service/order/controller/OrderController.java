package com.service.order.controller;

import com.service.order.service.OrderService;
import com.service.order.soap.Create;
import com.service.order.soap.Delete;
import com.service.order.soap.Read;
import com.service.order.soap.Response;
import com.service.order.soap.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.service.order.mapper.OrderMapper.toOrderModel;
import static com.service.order.mapper.OrderMapper.toServiceModel;

@Endpoint
@RequiredArgsConstructor
public class OrderController {

    public static final String SUCCESS = "Success";
    public static final String NAMESPACE_URI = "test";
    public static final String ERROR = "Error";

    private final OrderService orderService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Create")
    @ResponsePayload
    public Response createService(@RequestPayload Create request) {
        Response response = new Response();
        var order = toOrderModel(request);

        try {
            orderService.createService(order);
            response.setStatus(SUCCESS);
            response.setMessage("Service activated successfully");
        } catch (Exception e) {
            response.setStatus(ERROR);
            response.setErrorCode(400);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Read")
    @ResponsePayload
    public Response getService(@RequestPayload Read request) {
        Response response = new Response();

        try {
            var serviceOrder = orderService.getService(request.getServiceId());
            var service = toServiceModel(serviceOrder);
            response.setStatus(SUCCESS);
            response.setService(service);
        } catch (Exception e) {
            response.setStatus(ERROR);
            response.setErrorCode(404);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Update")
    @ResponsePayload
    public Response updateService(@RequestPayload Update request) {
        Response response = new Response();
        var order = toOrderModel(request);

        try {
            var serviceOrder = orderService.updateService(order);
            var service = toServiceModel(serviceOrder);
            response.setMessage("Service updated successfully");
            response.setStatus(SUCCESS);
            response.setService(service);
        } catch (Exception e) {
            response.setStatus(ERROR);
            response.setErrorCode(404);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Delete")
    @ResponsePayload
    public Response deleteService(@RequestPayload Delete request) {
        Response response = new Response();

        try {
            orderService.deleteService(request.getServiceId());
            response.setStatus(SUCCESS);
            response.setMessage("Service deleted successfully");
        } catch (Exception e) {
            response.setStatus(ERROR);
            response.setErrorCode(400);
            response.setErrorMessage(e.getMessage());
        }

        return response;

    }

}
