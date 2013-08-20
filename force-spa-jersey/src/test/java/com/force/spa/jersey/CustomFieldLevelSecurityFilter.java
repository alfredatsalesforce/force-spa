package com.force.spa.jersey;

import com.force.spa.FieldLevelSecurityFilter;

public class CustomFieldLevelSecurityFilter implements FieldLevelSecurityFilter {

    @Override
    public Permission getFieldLevelSecurityPermissions(String fieldName) {
        return new Permission(true, false);
    }

    @Override
    public String getRequesterId() {
        return null;
    }

}
