package io.tbc.spring.ws.article.config;

import com.medium.article.Article;
import io.tbc.spring.ws.article.client.ArticleClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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
    public ArticleClient articleClient(Jaxb2Marshaller jaxb2Marshaller){
        ArticleClient articleClient = new ArticleClient();
        articleClient.setDefaultUri("http://localhost:8080/ws/article");
        articleClient.setMarshaller(jaxb2Marshaller);
        articleClient.setUnmarshaller(jaxb2Marshaller);
        return articleClient;
    }
}
