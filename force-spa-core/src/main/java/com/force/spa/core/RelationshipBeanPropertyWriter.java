/*
 * Copyright, 2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

/**
 * A property bean writer which helps with the subtleties of serializing Salesforce relationships.
 * <p/>
 * If the related object contains a populated "id" member then you just want to send that id with a transformed property
 * name. If the related object has no "id" value, then you want to send the object in order to invoke special external
 * id lookup logic in the server that will try to find the related object on the server side based on a particular
 * object property value.
 * <p/>
 * <p/>
 * The external id algorithm is designed to help support request batching in situations where you don't know the
 * Salesforce id of the object up front. Details on the feature are beyond the scope if this doc. For more information
 * on this external id lookup algorithm you can refer to the Salesforce SOAP API documentation.
 */
public class RelationshipBeanPropertyWriter extends BeanPropertyWriter {

    private final ObjectDescriptor descriptor;
    private final String idPropertyName;

    public RelationshipBeanPropertyWriter(BeanPropertyWriter base, ObjectDescriptor descriptor) {
        super(base);
        this.descriptor = descriptor;
        this.idPropertyName = translateToIdName(base.getName());
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
        Object value = get(bean);
        if (value != null) {
            String id = RecordUtils.getId(descriptor, value);
            if (id != null) {
                jgen.writeStringField(idPropertyName, id);
                return;
            }
        }
        super.serializeAsField(bean, jgen, prov);
    }

    private static String translateToIdName(String relationshipName) {
        if (relationshipName.endsWith("__r")) {
            return relationshipName.substring(0, relationshipName.length() - 1) + "c"; // Id for a Custom Object
        } else {
            return relationshipName + "Id"; // Id for a Standard Salesforce object
        }
    }
}