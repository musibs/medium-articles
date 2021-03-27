package io.tbc.spring.ws.article.client;

import com.medium.article.GetArticleRequest;
import com.medium.article.GetArticleResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class ArticleClient extends WebServiceGatewaySupport {

    public GetArticleResponse getArticle(int id) {
        GetArticleRequest getArticleRequest = new GetArticleRequest();
        getArticleRequest.setId(id);
        return (GetArticleResponse) getWebServiceTemplate().marshalSendAndReceive(getArticleRequest);
    }
}
