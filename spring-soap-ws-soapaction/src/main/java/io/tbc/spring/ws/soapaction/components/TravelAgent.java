package io.tbc.spring.ws.soapaction.components;

import generated.FlightRequest;
import generated.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.math.BigInteger;
import java.util.List;

/**
 * @author somnath.musib
 */
@Component
public class TravelAgent {

    private WebServiceTemplate webServiceTemplate;

    @Autowired
    public TravelAgent(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public List<BigInteger> listFlights() {
        ObjectFactory objectFactory = new ObjectFactory();
        FlightRequest flightRequest = objectFactory.createFlightRequest();
        Object response = webServiceTemplate.marshalSendAndReceive(flightRequest, new SoapActionCallback("http://medium.com/TravelAgent/listFlights"));
        return (List<BigInteger>) response;
    }
}
