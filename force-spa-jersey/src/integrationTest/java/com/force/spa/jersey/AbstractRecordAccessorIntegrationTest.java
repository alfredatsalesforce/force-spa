/*
 * Copyright, 2013, SALESFORCE.com 
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.jersey;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;

import com.force.spa.FieldLevelSecurityFilter;
import com.force.spa.RecordAccessor;
import com.force.spa.RecordAccessorConfig;

public class AbstractRecordAccessorIntegrationTest {
    private static final SecureRandom secureRandom = new SecureRandom(RecordAccessorIntegrationTest.class.getName().getBytes());

    protected final PasswordAuthorizationConnector authorizationConnector = new PasswordAuthorizationConnector();
    protected final RecordAccessor accessor = new RecordAccessorFactory().newInstance(new RecordAccessorConfig(), authorizationConnector, new NoFieldLevelSecurity());
    protected final Set<Object> objects = new HashSet<Object>();

    @After
    public void deleteTestObjects() {
        for (Object object : objects) {
            try {
                accessor.delete(object);
            } catch (Exception e) {
                System.err.println("Failed to clean up object: " + e.toString());
            }
        }
    }

    protected String generateUniqueNumber() {
        return Integer.toHexString(secureRandom.nextInt());
    }

    protected Guild createGuild() {
        String uniqueSuffix = generateUniqueNumber();

        Guild guild = new Guild();
        guild.setName("Speed Cyclists - " + uniqueSuffix);
        guild.setDescription("A guild for bicycle racers - " + uniqueSuffix);
        String id = accessor.create(guild);
        guild.setId(id);
        objects.add(guild);

        return guild;
    }
    
    private class NoFieldLevelSecurity implements FieldLevelSecurityFilter {

        @Override
        public Permission getFieldLevelSecurityPermissions(String fieldName) {
            return null;
        }

        @Override
        public String getRequesterId() {
            return null;
        }
        
    }
}
