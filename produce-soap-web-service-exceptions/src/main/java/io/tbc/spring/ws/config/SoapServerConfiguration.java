package io.tbc.spring.ws.config;

import io.tbc.spring.ws.exception.resolver.CustomSoapFaultDefinitionExceptionResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

/**
 * @author somnath.musib
 */
@EnableWs
@Configuration
public class SoapServerConfiguration extends WsConfigurerAdapter {

    private static final String NAMESPACE_URI = "https://medium.com/article";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformSchemaLocations(true);
        return new ServletRegistrationBean<>(messageDispatcherServlet);
    }

    @Bean(name = "article")
    public Wsdl11Definition wsdl11Definition(XsdSchema xsdSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("ArticlePort");
        defaultWsdl11Definition.setLocationUri("/ws");
        defaultWsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        defaultWsdl11Definition.setSchema(xsdSchema);
        return defaultWsdl11Definition;
    }

    @Bean
    public XsdSchema xsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/article.xsd"));
    }

    @Bean
    public SoapFaultMappingExceptionResolver soapFaultMappingExceptionResolver() {
        SoapFaultMappingExceptionResolver soapFaultMappingExceptionResolver = new CustomSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        soapFaultDefinition.setFaultCode(SoapFaultDefinition.CLIENT);
        soapFaultMappingExceptionResolver.setDefaultFault(soapFaultDefinition);
        soapFaultMappingExceptionResolver.setOrder(1);
        return soapFaultMappingExceptionResolver;
    }

}
