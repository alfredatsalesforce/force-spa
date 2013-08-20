package com.force.spa.jersey;

import org.springframework.stereotype.Component;

import com.force.spa.FieldLevelSecurityFilter;

/**
 * A trivial implementation that assumes user always has full read/edit access to all fields.
 * */
@Component("fieldLevelSecurityFilter")
public class FullAccessFieldLevelSecurityFilter implements FieldLevelSecurityFilter {

    @Override
    public Permission getFieldLevelSecurityPermissions(String fieldName) {
        return new Permission(true, true);
    }

    @Override
    public String getRequesterId() {
        return null;
    }

}
