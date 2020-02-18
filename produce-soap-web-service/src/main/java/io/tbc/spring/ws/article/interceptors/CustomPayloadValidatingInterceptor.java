package io.tbc.spring.ws.article.interceptors;

import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;

import javax.xml.namespace.QName;

/**
 * @author somnath.musib
 */
public class CustomPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

    @Override
    public QName getDetailElementName() {
        return new QName("http://medium.com", "error", "app");
    }
}
