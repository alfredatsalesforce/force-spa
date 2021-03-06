/*
 * Copyright, 2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core;

import com.force.spa.RecordOperation;

import java.util.concurrent.ExecutionException;

public abstract class AbstractRecordOperation<T> implements RecordOperation<T> {
    private T result = null;
    private Throwable exception = null;
    private boolean done = false;

    @Override
    public final boolean isDone() {
        return done;
    }

    @Override
    public final T get() throws ExecutionException {
        if (done) {
            if (exception != null) {
                throw new ExecutionException(exception);
            } else {
                return result;
            }
        } else {
            throw new IllegalStateException("Operation not done yet");
        }
    }

    /**
     * Sets the successful result of the operation.
     */
    protected final void set(T result) {
        if (done) {
            throw new IllegalStateException("Operation is already done");
        }
        done = true;
        this.result = result;
    }

    /**
     * Sets the failure result of the operation.
     */
    protected final void setException(Throwable exception) {
        if (done) {
            throw new IllegalStateException("Operation is already done");
        }
        done = true;
        this.exception = exception;
    }
}
