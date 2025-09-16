package com.service.order.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


import java.util.List;

@EnableWs
@Configuration
public class WebServiceConfiguration implements WsConfigurer {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/som/*");
    }

    @Bean(name = "services")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema servicesSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("SomPort");
        definition.setLocationUri("/som");
        definition.setTargetNamespace("test");
        definition.setSchema(servicesSchema);
        return definition;
    }

    @Bean
    public XsdSchema servicesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schema.xsd"));
    }

    @Bean
    public CustomPayloadValidatingInterceptor validatingInterceptor() {
        CustomPayloadValidatingInterceptor interceptor = new CustomPayloadValidatingInterceptor();
        interceptor.setXsdSchema(servicesSchema());
        interceptor.setValidateRequest(true);
        interceptor.setValidateResponse(true);
        return interceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(validatingInterceptor());
    }

}
