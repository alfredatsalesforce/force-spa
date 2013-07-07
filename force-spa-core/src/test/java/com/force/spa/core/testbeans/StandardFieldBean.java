/*
 * Copyright, 2012-2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core.testbeans;

import com.force.spa.SalesforceField;
import com.force.spa.SalesforceObject;

import java.util.Date;

/**
 * A test bean that includes the standard fields.
 */
@SuppressWarnings("ALL")
@SalesforceObject
public class StandardFieldBean {

    @SalesforceField(name = "Id")
    private String id;

    @SalesforceField(name = "Name")
    private String name;

    @SalesforceField(name = "CreatedBy")
    private UserMoniker createdBy;

    @SalesforceField(name = "CreatedDate")
    private Date createdDate;

    @SalesforceField(name = "LastModifiedBy")
    private UserMoniker lastModifiedBy;

    @SalesforceField(name = "LastModifiedDate")
    private Date lastModifiedDate;

    @SalesforceField(name = "Owner")
    private UserMoniker owner;

    public UserMoniker getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserMoniker createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserMoniker getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(UserMoniker lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserMoniker getOwner() {
        return owner;
    }

    public void setOwner(UserMoniker owner) {
        this.owner = owner;
    }
}
