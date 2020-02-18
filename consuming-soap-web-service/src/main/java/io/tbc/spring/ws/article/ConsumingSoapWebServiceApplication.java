package io.tbc.spring.ws.article;

import com.medium.article.Article;
import io.tbc.spring.ws.article.client.ArticleClient;
import io.tbc.spring.ws.article.config.SoapClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ConsumingSoapWebServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ConsumingSoapWebServiceApplication.class, args);
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        ArticleClient articleClient = annotationConfigApplicationContext.getBean(ArticleClient.class);
        System.out.println(articleClient.getArticle(1).getArticle().getName());
    }

}
