/*
 * Copyright, 2012-2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.force.spa.ObjectDefinitionException;

/**
 * Extra metadata about a Salesforce Object above and beyond that normally managed by Jackson. The information is
 * collected at the same time as the core Jackson metadata (introspection time) and references some of the standard
 * Jackson classes.
 */
public final class ObjectDescriptor implements Serializable {

    private static final long serialVersionUID = 1998217301661330641L;

    public static final String ID_FIELD_NAME = "Id";
    public static final String ATTRIBUTES_FIELD_NAME = "attributes";

    private final String name;
    private List<FieldDescriptor> fields;
    private final Map<String, FieldDescriptor> fieldsByName;
    private boolean hasFieldLevelSecurityEnabledField;

    ObjectDescriptor(String name) {
        this.name = name;
        this.fields = Collections.emptyList();
        this.fieldsByName = new HashMap<String, FieldDescriptor>();
    }

    void initializeFields(List<FieldDescriptor> fields) {
        if (this.fields.equals(Collections.emptyList())) {
            this.fields = Collections.unmodifiableList(fields);

            for (FieldDescriptor field : fields) {
                fieldsByName.put(field.getName(), field);
                hasFieldLevelSecurityEnabledField = hasFieldLevelSecurityEnabledField || field.isFieldLevelSecurityEnabled();
            }
        } else {
            throw new IllegalStateException("Fields have already been initialized");
        }

        if (hasAttributesField()) {
            if (!Map.class.isAssignableFrom(getAttributesField().getType()))
                throw new ObjectDefinitionException(name, "'attributes' field has wrong Java type, must be Map<String, String>");
        }

        if (hasIdField()) {
            if (!String.class.isAssignableFrom(getIdField().getType()))
                throw new ObjectDefinitionException(name, "'id' field has wrong Java type, must be String");
        }
    }

    public boolean hasField(String name) {
        return fieldsByName.get(name) != null;
    }

    public boolean hasAttributesField() {
        return hasField(ATTRIBUTES_FIELD_NAME);
    }

    public boolean hasIdField() {
        return hasField(ID_FIELD_NAME);
    }
    
    public boolean hasFieldLevelSecurityEnabledField() {
        return hasFieldLevelSecurityEnabledField;
    }

    public FieldDescriptor getField(String name) {
        FieldDescriptor field = fieldsByName.get(name);
        if (field == null) {
            throw new IllegalStateException(String.format("The object does not have a \'%s\' field", name));
        }
        return field;
    }

    public FieldDescriptor getIdField() {
        return getField(ID_FIELD_NAME);
    }

    public FieldDescriptor getAttributesField() {
        return getField(ATTRIBUTES_FIELD_NAME);
    }

    public String getName() {
        return name;
    }

    public List<FieldDescriptor> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).append(fields).build();
    }
}
