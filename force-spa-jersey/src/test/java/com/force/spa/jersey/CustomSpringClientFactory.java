/*
 * Copyright, 2013, SALESFORCE.com 
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.jersey;

import com.force.spa.AuthorizationConnector;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample custom client factory.
 */
public class CustomSpringClientFactory implements FactoryBean<Client> {
    private final ClientFactory internalFactory = new ClientFactory();

    @Autowired
    private AuthorizationConnector authorizationConnector;

    /**
     * A filter for customizing outbound requests.
     */
    private final ClientFilter filter = new ClientFilter() {
        @Override
        public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
            clientRequest.getHeaders().add(HttpHeaders.USER_AGENT, "Work.com");
            return getNext().handle(clientRequest);
        }
    };

    @Override
    public Client getObject() throws Exception {
        Client client = internalFactory.newInstance(authorizationConnector);
        client.addFilter(filter);
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return Client.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
