/*
 * Copyright, 2013, SALESFORCE.com 
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.jersey;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.force.spa.AuthorizationConnector;
import com.force.spa.FieldLevelSecurityFilter;
import com.force.spa.RecordAccessor;
import com.sun.jersey.api.client.Client;

@ContextConfiguration(locations = {"classpath:com/force/spa/jersey/springCustomInjectionContext.xml"})
public class SpringCustomInjectionTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AuthorizationConnector authorizationConnector;

    @Autowired
    private Client client;

    @Autowired
    private RecordAccessor accessor;
    
    @Autowired
    private FieldLevelSecurityFilter fieldLevelSecurityFilter;

    @Test
    public void testAutowiring() {
        assertNotNull(authorizationConnector);
        assertNotNull(client);
        assertNotNull(accessor);
        assertNotNull(fieldLevelSecurityFilter);

        assertTrue(
            "Should be instance of CustomAuthorizationConnector",
            authorizationConnector instanceof CustomAuthorizationConnector);
        
        assertTrue(
            "Should be instance of CustomFieldLevelSecurityFilter", 
            fieldLevelSecurityFilter instanceof CustomFieldLevelSecurityFilter);
    }
}
