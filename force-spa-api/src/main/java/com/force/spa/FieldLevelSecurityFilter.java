package com.force.spa;

public interface FieldLevelSecurityFilter {

    Permission getFieldLevelSecurityPermissions(String fieldName);
    String getRequesterId();
    
    static class Permission {
        private final boolean permissionRead;
        private final boolean permissionEdit;
        
        public Permission(boolean permissionRead, boolean permissionEdit) {
            this.permissionEdit = permissionEdit;
            this.permissionRead = permissionRead;
        }
        
        public boolean isPermissionRead() {
            return permissionRead;
        }
        
        public boolean isPermissionEdit() {
            return permissionEdit;
        }
    }
}
