package io.tbc.spring.ws;

import io.tbc.spring.ws.client.ArticleClient;
import io.tbc.spring.ws.config.SoapClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringSoapUsernamePasswordAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSoapUsernamePasswordAuthClientApplication.class, args);
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        ArticleClient articleClient = annotationConfigApplicationContext.getBean(ArticleClient.class);
        System.out.println(articleClient.getArticle(1).getArticle().getName());
    }

}
