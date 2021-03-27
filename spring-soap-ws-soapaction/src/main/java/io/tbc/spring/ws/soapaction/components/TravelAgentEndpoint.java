package io.tbc.spring.ws.soapaction.components;

import generated.FlightResponse;
import generated.ObjectFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

/**
 * @author somnath.musib
 */
public class TravelAgentEndpoint {

    @ResponsePayload
    @SoapAction(value = "http://medium.com/TravelAgent/listFlights")
    public FlightResponse listFlights(MessageContext messageContext) {
        WebServiceMessage webServiceMessage = messageContext.getRequest();
        SoapMessage soapMessage = (SoapMessage) webServiceMessage;
        System.out.println(soapMessage.getSoapAction());

        ObjectFactory objectFactory = new ObjectFactory();
        FlightResponse flightResponse = objectFactory.createFlightResponse();
        flightResponse.getFlightNumber().add("DH-124");
        flightResponse.getFlightNumber().add("SG-124");
        flightResponse.getFlightNumber().add("AL-124");
        flightResponse.getFlightNumber().add("OY-124");

        return flightResponse;
    }
}
