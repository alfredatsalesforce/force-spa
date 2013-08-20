/*
 * Copyright, 2012-2013, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.force.spa.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.force.spa.FieldLevelSecurityFilter;
import com.force.spa.core.testbeans.FieldLevelSecurityEnabledBean;
import com.force.spa.core.testbeans.PolymorphicFieldBean;
import com.force.spa.core.testbeans.RecursiveBean;
import com.force.spa.core.testbeans.SimpleBean;

public class SoqlBuilderTest {

    private static final ObjectMappingContext mappingContext = new ObjectMappingContext();

    @Test
    public void testBasicWildcard() throws Exception {
        String soqlTemplate = "select * from SimpleBean where Id = '012345678901234'";
        String expectedSoql = "select Id,Name,Description from SimpleBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(SimpleBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }

    @Test
    public void testWildcardWithOnePartPrefix() throws Exception {
        String soqlTemplate = "select Prefix1.* from SimpleBean where Id = '012345678901234'";
        String expectedSoql = "select Prefix1.Id,Prefix1.Name,Prefix1.Description from SimpleBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(SimpleBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }

    @Test
    public void testWildcardWithTwoPartPrefix() throws Exception {
        String soqlTemplate = "select Prefix1.Prefix2.* from SimpleBean where Id = '012345678901234'";
        String expectedSoql = "select Prefix1.Prefix2.Id,Prefix1.Prefix2.Name,Prefix1.Prefix2.Description from SimpleBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(SimpleBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }

    @Test
    public void testRelationshipSubquery() throws Exception {
        String soqlTemplate = "select (select RelatedBean.* from SimpleBean.RelatedBeans) from SimpleBean where Id = '012345678901234'";
        String expectedSoql = "select (select RelatedBean.Id,RelatedBean.Name,RelatedBean.Description from SimpleBean.RelatedBeans) from SimpleBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(SimpleBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }

    @Test
    public void testRecursiveTypeReferences() throws Exception {
        String soqlTemplate = "select * from RecursiveBean where Id = '012345678901234'";
        String expectedSoql = "select Id,RecursiveBean.Id,RecursiveBean.RecursiveBean.Id,RecursiveBean.RecursiveBean.RecursiveBean.Id,RecursiveBean.RecursiveBean.RecursiveBean.RecursiveBean.Id,RecursiveBean.RecursiveBean.RecursiveBean.RecursiveBean.RecursiveBean.Id from RecursiveBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(RecursiveBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }

    @Test
    public void testPolymorphicRelationships() throws Exception {
        String soqlTemplate = "select * from PolymorphicFieldBean where Id = '012345678901234'";
        String expectedSoql = "select Id,TYPEOF Value1 WHEN SimpleBean THEN Id,Name,Description WHEN NoAttributesBean THEN Id,Name END,TYPEOF Value2 WHEN SimpleBean THEN Id,Name,Description WHEN NoAttributesBean THEN Id,Name END from PolymorphicFieldBean where Id = '012345678901234'";

        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(PolymorphicFieldBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }
    
    @Test
    public void testFieldLevelSecurityDisabled() {
        String soqlTemplate = "select * from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        String expectedSoql = "select Id from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        
        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(FieldLevelSecurityEnabledBean.class), new MockFieldLevelSecurityFilter()).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }
    
    @Test
    public void testFieldLevelSecurityEnabled() {
        String soqlTemplate = "select * from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        String expectedSoql = "select Id,aField from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        
        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(FieldLevelSecurityEnabledBean.class), 
                new TestFieldLevelSecurityFilter("aField", true, true)).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }
    
    @Test
    public void testFieldLevelSecurityReadOnly() {
        String soqlTemplate = "select * from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        String expectedSoql = "select Id,aField from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        
        String soql = new SoqlBuilder(mappingContext.getObjectDescriptor(FieldLevelSecurityEnabledBean.class), 
                new TestFieldLevelSecurityFilter("aField", true, false)).soqlTemplate(soqlTemplate).build();
        assertThat(soql, is(equalTo(expectedSoql)));
    }
    
    @Test
    public void testSoqlBuilderCache() {
        String soqlTemplate = "select * from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        String expectedSoql = "select Id,aField from FieldLevelSecurityEnabledBean where Id = '0123456789'";
        
        String requesterId = "user-123";
        String fieldLevelSecurityEnabled = new SoqlBuilder(mappingContext.getObjectDescriptor(FieldLevelSecurityEnabledBean.class), 
                new TestFieldLevelSecurityFilter("aField", true, true, requesterId)).soqlTemplate(soqlTemplate).build();
        assertThat(fieldLevelSecurityEnabled, is(equalTo(expectedSoql)));
        
        // resulting soql doesn't change since SoqlBuilder has its own cache
        String fieldLevelSecurityDisabled = new SoqlBuilder(mappingContext.getObjectDescriptor(FieldLevelSecurityEnabledBean.class), 
                new MockFieldLevelSecurityFilter(requesterId)).soqlTemplate(soqlTemplate).build();
        assertThat(fieldLevelSecurityDisabled, is(equalTo(expectedSoql)));
    }
    
    private class TestFieldLevelSecurityFilter implements FieldLevelSecurityFilter {

        private final String requesterId;
        private final Permission permission;
        private final String field;
        
        private TestFieldLevelSecurityFilter(String field, boolean permissionRead, boolean permissionEdit) {
            this(field, permissionRead, permissionEdit, String.valueOf(System.nanoTime()));
        }
        
        private TestFieldLevelSecurityFilter(String field, boolean permissionRead, boolean permissionEdit, String requesterId) {
            this.permission = new Permission(permissionRead, permissionEdit);
            this.field = field;
            this.requesterId = requesterId;
        }
        
        @Override
        public Permission getFieldLevelSecurityPermissions(String fieldName) {
            return field.equals(fieldName) ? permission : null;
        }

        @Override
        public String getRequesterId() {
            return requesterId;
        }
        
    }
    
    private class MockFieldLevelSecurityFilter implements FieldLevelSecurityFilter {

        private String requesterId;
        
        private MockFieldLevelSecurityFilter() {
            this(null);
        }
        
        private MockFieldLevelSecurityFilter(String requesterId) {
            this.requesterId = requesterId;
        }
        
        @Override
        public Permission getFieldLevelSecurityPermissions(String fieldName) {
            return null;
        }

        @Override
        public String getRequesterId() {
            return requesterId;
        }
        
    }
}
