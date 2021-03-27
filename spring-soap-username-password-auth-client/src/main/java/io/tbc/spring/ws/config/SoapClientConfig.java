package io.tbc.spring.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import io.tbc.spring.ws.client.ArticleClient;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;

import java.util.Properties;

/**
 * @author somnath.musib
 */
@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.medium.article");
        return jaxb2Marshaller;
    }

    @Bean
    public ArticleClient articleClient(Jaxb2Marshaller jaxb2Marshaller) {
        ArticleClient articleClient = new ArticleClient();
        articleClient.setDefaultUri("http://localhost:9090/ws/article");
        articleClient.setMarshaller(jaxb2Marshaller);
        articleClient.setUnmarshaller(jaxb2Marshaller);
        ClientInterceptor[] interceptors = new ClientInterceptor[]{wss4jSecurityInterceptor()};
        articleClient.setInterceptors(interceptors);
        return articleClient;
    }

    @Bean
    public Wss4jSecurityInterceptor wss4jSecurityInterceptor() {
        Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
        wss4jSecurityInterceptor.setSecurementActions("Timestamp UsernameToken");
        wss4jSecurityInterceptor.setSecurementUsername("user");
        wss4jSecurityInterceptor.setSecurementPassword("password");
        return wss4jSecurityInterceptor;
    }
}
