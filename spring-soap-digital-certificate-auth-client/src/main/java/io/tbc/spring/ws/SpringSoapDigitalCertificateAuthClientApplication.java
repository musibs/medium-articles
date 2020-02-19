package io.tbc.spring.ws;

import com.medium.article.Article;
import com.medium.article.GetArticleResponse;
import io.tbc.spring.ws.client.ArticleClient;
import io.tbc.spring.ws.config.SoapClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringSoapDigitalCertificateAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSoapDigitalCertificateAuthClientApplication.class, args);
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        ArticleClient articleClientBean = annotationConfigApplicationContext.getBean(ArticleClient.class);
        GetArticleResponse article = articleClientBean.getArticle(1);
        System.out.println(article.getArticle().getName());
    }

}
