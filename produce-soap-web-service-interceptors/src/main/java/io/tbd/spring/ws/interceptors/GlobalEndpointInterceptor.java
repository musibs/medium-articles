package io.tbd.spring.ws.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * @author somnath.musib
 */
public class GlobalEndpointInterceptor implements EndpointInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GlobalEndpointInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Global Request handling: Request message " + messageContext.getRequest().getPayloadResult());
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Global response handling: Response message " + messageContext.getRequest().getPayloadResult());
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        logger.error("Global exception handling");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        logger.error("Global after completion handling");
    }
}
