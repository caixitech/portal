package org.helloframework.mybatis.definition;

/**
 * @author lanjian
 */
public enum IdentityDialect {
    DB2("VALUES IDENTITY_VAL_LOCAL()"),
    MYSQL("SELECT LAST_INSERT_ID()"),
    SQLSERVER("SELECT SCOPE_IDENTITY()"),
    CLOUDSCAPE("VALUES IDENTITY_VAL_LOCAL()"),
    DERBY("VALUES IDENTITY_VAL_LOCAL()"),
    HSQLDB("CALL IDENTITY()"),
    SYBASE("SELECT @@IDENTITY"),
    DB2_MF("SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1"),
    INFORMIX("select dbinfo('sqlca.sqlerrd1') from systables where tabid=1");

    private String identity;

    IdentityDialect(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }


}
