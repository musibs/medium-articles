package io.tbd.spring.ws.config;

import io.tbd.spring.ws.interceptors.GlobalEndpointInterceptor;
import io.tbd.spring.ws.interceptors.LocalEndpointInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadRootSmartSoapEndpointInterceptor;
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
    private static final String LOCAL_PART = "getArticleRequest";

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(new GlobalEndpointInterceptor());
        interceptors.add(new PayloadRootSmartSoapEndpointInterceptor(new LocalEndpointInterceptor(), NAMESPACE_URI, LOCAL_PART));
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformSchemaLocations(true);
        return new ServletRegistrationBean<>(messageDispatcherServlet);
    }

    @Bean(name="article")
    public Wsdl11Definition wsdl11Definition(XsdSchema xsdSchema){
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("ArticlePort");
        defaultWsdl11Definition.setLocationUri("/ws");
        defaultWsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        defaultWsdl11Definition.setSchema(xsdSchema);
        return defaultWsdl11Definition;
    }

    @Bean
    public XsdSchema xsdSchema(){
        return new SimpleXsdSchema(new ClassPathResource("xsd/article.xsd"));
    }


}
