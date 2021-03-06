/*
 * Copyright, 2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core.rest;

import com.force.spa.RecordResponseException;
import com.force.spa.RestConnector;
import com.force.spa.UpdateRecordOperation;
import com.force.spa.core.ObjectDescriptor;
import com.force.spa.core.ObjectMappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

class RestUpdateRecordOperation<T> extends AbstractRestRecordOperation<Void> implements UpdateRecordOperation<T> {
    private static final Logger log = LoggerFactory.getLogger(RestUpdateRecordOperation.class);

    private final String id;
    private final T record;

    public RestUpdateRecordOperation(String id, T record) {
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        if (record == null)
            throw new IllegalArgumentException("record must not be null");

        this.id = id;
        this.record = record;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public T getRecord() {
        return record;
    }

    @Override
    public void start(RestConnector connector, ObjectMappingContext mappingContext) {
        final ObjectDescriptor descriptor = mappingContext.getRequiredObjectDescriptor(record.getClass());

        URI uri = URI.create("/sobjects/" + descriptor.getName() + "/" + id);
        String json = encodeRecordForUpdate(mappingContext, record);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Update %s %s: %s", descriptor.getName(), id, json));
        }

        connector.patch(uri, json, new RestConnector.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("...Updated %s %s", descriptor.getName(), id));
                }
                set(null);
            }

            @Override
            public void onFailure(RuntimeException exception) {
                setException(exception);
            }
        });
    }

    private static String encodeRecordForUpdate(ObjectMappingContext mappingContext, Object record) {
        try {
            return mappingContext.getObjectWriterForUpdate().writeValueAsString(record);
        } catch (IOException e) {
            throw new RecordResponseException("Failed to encode record as JSON", e);
        }
    }
}
