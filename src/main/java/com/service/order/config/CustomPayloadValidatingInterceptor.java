package com.service.order.config;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

public class CustomPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

    @Override
    protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors) {
        if (messageContext.getResponse() instanceof SoapMessage response) {
            for (SAXParseException error : errors) {
                response.getSoapBody().addClientOrSenderFault(error.getMessage(),
                        null);
            }
        }
        return false;
    }
}
