package io.tbc.spring.ws.config;

import io.tbc.spring.ws.client.ArticleClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;

import java.io.IOException;

/**
 * @author somnath.musib
 */
@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.medium.article");
        return jaxb2Marshaller;
    }

    @Bean
    public ArticleClient articleClient(Jaxb2Marshaller jaxb2Marshaller) throws Exception {
        ArticleClient articleClient = new ArticleClient();
        articleClient.setDefaultUri("http://localhost:9090/ws/article");
        articleClient.setMarshaller(jaxb2Marshaller);
        articleClient.setUnmarshaller(jaxb2Marshaller);
        ClientInterceptor[] interceptors = new ClientInterceptor[]{wss4jSecurityInterceptor()};
        articleClient.setInterceptors(interceptors);
        return articleClient;
    }

    @Bean
    public Wss4jSecurityInterceptor wss4jSecurityInterceptor() throws Exception {
        Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
        wss4jSecurityInterceptor.setSecurementActions("Timestamp Signature Encrypt");

        //Sign the request
        wss4jSecurityInterceptor.setSecurementUsername("client");
        wss4jSecurityInterceptor.setSecurementPassword("changeit");
        wss4jSecurityInterceptor.setSecurementSignatureCrypto(cryptoFactoryBean().getObject());

        // Encrypt the request
        wss4jSecurityInterceptor.setSecurementEncryptionUser("server-public");
        wss4jSecurityInterceptor.setSecurementEncryptionCrypto(cryptoFactoryBean().getObject());
        wss4jSecurityInterceptor.setSecurementEncryptionParts("{Content}{https://medium.com/article}getArticleRequest");
        wss4jSecurityInterceptor.setSecurementSignatureKeyIdentifier("DirectReference");

        return wss4jSecurityInterceptor;
    }
    @Bean
    public CryptoFactoryBean cryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword("changeit");
        cryptoFactoryBean.setKeyStoreLocation(new ClassPathResource("client.jks"));
        return cryptoFactoryBean;
    }
}
