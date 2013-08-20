/*
 * Copyright, 2012-2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.jersey;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.force.spa.ApiVersion;
import com.force.spa.AuthorizationConnector;
import com.force.spa.FieldLevelSecurityFilter;
import com.force.spa.RecordAccessor;
import com.force.spa.RecordAccessorConfig;
import com.sun.jersey.api.client.Client;

/**
 * A Spring factory for instances of {@link RecordAccessor} that use a {@link JerseyRestConnector} for communications.
 */
@Component("recordAccessorFactory")
public class SpringRecordAccessorFactory implements FactoryBean<RecordAccessor> {

    private final RecordAccessorFactory internalFactory = new RecordAccessorFactory();

    @Autowired
    private Client client;

    @Autowired
    private AuthorizationConnector authorizationConnector;
    
    @Autowired
    private FieldLevelSecurityFilter fieldLevelSecurityFilter;

    @Autowired(required = false)
    private RecordAccessorConfig config = new RecordAccessorConfig();

    private ApiVersion apiVersion = null;

    /**
     * Sets the Salesforce API version used by the generated {@link RecordAccessor} instances.
     * <p/>
     * If a version is not configured with this method then the default is to use the highest version supported by the
     * Salesforce server.
     *
     * @param apiVersion a Salesforce API version (for example: "28.0")
     */
    public void setApiVersion(ApiVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

    ApiVersion getApiVersion() {
        return this.apiVersion;
    }

    @Override
    public RecordAccessor getObject() {
        return internalFactory.newInstance(config, authorizationConnector, client, apiVersion, fieldLevelSecurityFilter);
    }

    @Override
    public Class<?> getObjectType() {
        return RecordAccessor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
