package io.tbc.spring.ws.exception.resolver;

import io.tbc.spring.ws.exception.ArticleNotFoundException;
import io.tbc.spring.ws.exception.model.ArticleFault;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

/**
 * @author somnath.musib
 */
public class CustomSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("Code");
    private static final QName DESCRIPTION = new QName("Description");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        logger.error("Processing exception " + ex);
        if (ex instanceof ArticleNotFoundException) {
            ArticleFault articleFault = ((ArticleNotFoundException) ex).getArticleFault();
            SoapFaultDetail soapFaultDetail = fault.addFaultDetail();
            soapFaultDetail.addFaultDetailElement(CODE).addText(articleFault.getCode());
            soapFaultDetail.addFaultDetailElement(DESCRIPTION).addText(articleFault.getDescription());
        }
    }
}
