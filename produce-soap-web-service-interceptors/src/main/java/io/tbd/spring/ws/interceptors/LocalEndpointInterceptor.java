package io.tbd.spring.ws.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * @author somnath.musib
 */
public class LocalEndpointInterceptor implements EndpointInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GlobalEndpointInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Handling local request. Request message "+messageContext.getRequest().getPayloadSource());
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Handling local response. Request message "+messageContext.getRequest().getPayloadResult());
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Local exception handling");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        logger.error("Local after completion handling");
    }
}
