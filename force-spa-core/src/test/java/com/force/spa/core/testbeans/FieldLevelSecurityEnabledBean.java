package com.force.spa.core.testbeans;

import com.force.spa.SalesforceField;
import com.force.spa.SalesforceObject;
import com.force.spa.record.Record;

@SalesforceObject
public class FieldLevelSecurityEnabledBean extends Record {

    @SalesforceField(name = "aField", fieldLevelSecurity = true)
    private String aField;

    public String getaField() {
        return aField;
    }

    public void setaField(String aField) {
        this.aField = aField;
    }
    
}
