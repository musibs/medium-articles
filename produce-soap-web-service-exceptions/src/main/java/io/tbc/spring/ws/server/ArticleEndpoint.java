package io.tbc.spring.ws.server;

import https.medium_com.article.Article;
import https.medium_com.article.GetArticleRequest;
import https.medium_com.article.GetArticleResponse;
import https.medium_com.article.ObjectFactory;
import io.tbc.spring.ws.exception.ArticleNotFoundException;
import io.tbc.spring.ws.exception.model.ArticleFault;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * @author somnath.musib
 */
@Endpoint
public class ArticleEndpoint {

    private static final String NAMESPACE_URI = "https://medium.com/article";

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getArticleRequest")
    public GetArticleResponse getArticle(@RequestPayload GetArticleRequest articleRequest){

        if(articleRequest.getId() != 1){
            throw new ArticleNotFoundException("Exception Occurred ",
                    new ArticleFault("NOT_FOUND", "Article with id "+articleRequest.getId()+" does not exists"));
        }

        Article article = new Article();
        article.setId(1);
        article.setName("Publish a SOAP Web Service With Spring Boot");
        GetArticleResponse getArticleResponse = new ObjectFactory().createGetArticleResponse();
        getArticleResponse.setArticle(article);
        return getArticleResponse;
    }
}
