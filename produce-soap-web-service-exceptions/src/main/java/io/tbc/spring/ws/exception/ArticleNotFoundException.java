package io.tbc.spring.ws.exception;

import io.tbc.spring.ws.exception.model.ArticleFault;

/**
 * @author somnath.musib
 */
public class ArticleNotFoundException extends RuntimeException {

    private ArticleFault articleFault;

    public ArticleNotFoundException(String message, ArticleFault articleFault) {
        super(message);
        this.articleFault = articleFault;
    }

    public ArticleNotFoundException(String message, Throwable cause, ArticleFault articleFault) {
        super(message, cause);
        this.articleFault = articleFault;
    }

    public ArticleFault getArticleFault() {
        return articleFault;
    }

    public void setArticleFault(ArticleFault articleFault) {
        this.articleFault = articleFault;
    }
}
