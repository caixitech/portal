package ${packageName}.pojo;

import org.helloframework.mybatis.annotations.Column;
import org.helloframework.mybatis.annotations.Id;
import org.helloframework.mybatis.annotations.Table;
import java.util.Date;
/**
* Created by codegen
*/
@Table(table = "${tableName}")
public class ${pojoName} {
<#list columnInfos as columnInfo>

    @Column(column = "${columnInfo.columnName}")
    private ${columnInfo.columnType} ${columnInfo.javaColumnName?uncap_first};

    public ${columnInfo.columnType} get${columnInfo.javaColumnName}() {
        return ${columnInfo.javaColumnName?uncap_first};
    }

    public void set${columnInfo.javaColumnName}(${columnInfo.columnType} ${columnInfo.javaColumnName?uncap_first}) {
        this.${columnInfo.javaColumnName?uncap_first} = ${columnInfo.javaColumnName?uncap_first};
    }

</#list>
}
